package com.example.shoppingmall.domain.order.application;

import com.example.shoppingmall.domain.item.dao.ItemStockRepository;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.order.dao.OrderRepository;
import com.example.shoppingmall.domain.order.domain.Orders;
import com.example.shoppingmall.domain.order.dto.OrderItemRequest;
import com.example.shoppingmall.domain.order.dto.OrderItemResponse;
import com.example.shoppingmall.domain.order.dto.OrderRequest;
import com.example.shoppingmall.domain.order.dto.OrderResponse;
import com.example.shoppingmall.domain.order.excepction.OrderException;
import com.example.shoppingmall.domain.order.type.OrderCancelOption;
import com.example.shoppingmall.domain.order.type.OrderResult;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.global.exception.ErrorCode;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.shoppingmall.domain.order.type.OrderCancelOption.*;
import static com.example.shoppingmall.domain.order.type.OrderCancelOption.CANCEL_ENTIRE_ORDER;
import static com.example.shoppingmall.domain.order.type.OrderResult.*;
import static com.example.shoppingmall.domain.order.type.OrderResult.SUCCESS;
import static com.example.shoppingmall.domain.order.type.OrderStatus.ACCEPT;
import static com.example.shoppingmall.global.exception.ErrorCode.ITEM_UNAVAILABLE;
import static com.example.shoppingmall.global.exception.ErrorCode.UNPROCESSABLE_ORDER;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderServiceV1 {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ItemStockRepository itemStockRepository;

    private final RedissonClient redissonClient;

    // 읽으셔도 되고, 안 읽으셔도 됩니다...
    // 제 생각이 정리가 안돼서 주저리주저리 적어놓은 겁니다. (pr에만 적을까 하다가 코드랑 바로 볼 수 있는게 좋을 듯 하여... 물론 노션에도 적어놓을 겁니다.)

    /** 일단 테스트 결과, 동시성문제는 해결되지 못한 상태
     *
     *  [동시성을 해결하기 위해 선택한 방안]
     *
     * 비관적 락, 낙관적 락, 네임드락 등 살펴보았는데, 각자 장단점이 있었고 현재 적용하기 적합한게 어떤 것인지 판단이 잘 서지 않았다.
     * (일단 비관적 락은 데이터 정합성, 충돌이 잦은 상황에서 좋다고 했지만 row 자체에 lock를 걸어서 다른 기능에서 조회하려고 하면 안되지 않나..? 싶었다.)
     *
     *
     * 이미 프로젝트에서 레디스를 사용하고 있기때문에, 레디스를 활용한 분산 락 구현을 고려해보았다.
     *
     * - lettuce
     *   - setNx 명령어를 활용 ->  SpinLock 방식 (재시도 로직을 직접 작성해야함)
     *   - 스핀 락은 레디스에 부담이 된다고 함 (락을 사용할 수 있는지 반복적 확인하면서 획득 시도)
     *
     * - redisson
     *   - pub, sub 기반의 lock 이 이미 구현돼있다. (타임아웃 기능도 간편하게 사용할 수 있다.)
     *   - lettuce 와 다르게 대부분의 경우에는 별도의 retry 로직을 작성하지 않아도 됨 (대신 별도의 라이브러리 추가 필요)
     *  깊게 들어가면 Lettuce 보다 어렵다는 말도 있으나, 일단 시간이 얼마 남지 않았고, 동시성에 대해 다뤄본 적이 한번도 없는 상황에서
     *  그나마 빠르게 적용해볼 수 있는 부분이라 생각했다.
     *
     *  -> redisson 선택
     *
     *
     * 판단:
     * - 만약 10종류의 물품을 주문요청 시 10개의 itemStock에 한번에 lock을 걸고 10개에 대한 주문이 완료될때까지 해제 하지 않는다..? 이건 아닌 것 같았다.
     * - 한 물품 씩 락을 거는게 맞다고 판단
     *
     * - 일단 락을 걸게 되면 트랜잭션 안에 있으면 안된다는 점은 인지하긴 했다.
     *   트랜잭션이 끝나면서 변경사항이 db에 반영되기 전에 lock 이 풀리면서 다른 사용자가 같은 물품에 접근했을 땐, 변경사항이 반영되지 않은 상태에 접근할 확률이 매우 높다.
     *
     *   그래서 일단 lock이 걸려야하는 작업의 메서드는 별도로 구현 한 뒤  lock에 관한 메서드가 감싸는 형태로 구현하기로 하였다.
     *   (processOrderItem 메서드 안에서 orderItem 메서드 를 호출하는 형태)
     *
     *  하지만 걸리는 부분:
     *  주문 요청한 물품 한 개 에 대한 작업은 lock을 걸 수 있었지만,
     *
     *  여러 물품을 주문한 상태고,
     *
     *  orderItem 를 생성하려면 Order 엔티티를 넣어주거나, Order 에서 양방향 메서드로 add 시키면 되는데
     *
     *  orderItem을 매번 따로 저장하면 db 에 오가는 횟수가 너무 많지 않나..? 싶다고 생각했고
     *  Order를 생성한 뒤 orderItem 들을 차례대로 add 시킨 뒤 Order를 저장하면 db에 오가는 횟수를 줄일 수 있다고 생각했다. (다른 기능구현 시에도 그렇게 했었다.)
     *
     *  여러 물품에 대한 작업이 결국 하나의 Order에 들어가야하기 때문에,
     *  아래와 같이 구현했다.
     *
     *  orderItems[ user를 찾아오고, order 엔티티 객체 생성(아직 영속화X)   processOrderItem[ 락 획득   orderItem[ 주문 로직 (전달받은 order 엔티티에 add ) ]   락 해제 ]   order 저장 ]
     *
     *  결국 order를 save 을 해야해서 가장 밖에 있는 orderItems에도 트랜잭션을 적용해야했다.
     *
     *  lock 이 감싸는 orderItem 의 트랜잭션이 가장 밖에 있는 orderItems의 트랜잭션에 참여하면 안된다고 생각해서,
     *  상위 트랜잭션과는 독립적으로 동작하게 하기 위해
     *  orderItem 에 @Transactional(propagation = Propagation.REQUIRES_NEW) 를 적용해주었다.
     *  (그러면 orderItem 이 한번 씩 끝날 때 마다  변경감지로 db에 반영이 될 것이라고 생각했다.)
     *
     *
     *  ### 나의 예상 쿼리 순서
     *
     * 1. **`User` 조회 (SELECT)**: user와 cart를 조회하는 쿼리 (패치조인)
     * 2. **각 주문 아이템 처리 (`processOrderItem`)**
     *     - **재고 조회 (SELECT FOR UPDATE)**: 락을 건 후 재고 정보를 조회
     *     - **재고 업데이트 (UPDATE)**: 재고 차감
     * 3. **주문 저장 (INSERT)**: 주문과 그에 따른 아이템들을 저장
     * 4. **각 주문 아이템 저장 (INSERT)**: 주문 아이템들이 `orders`에 추가됨
     *
     * 이 구조에서 락이 걸린 후 재고를 조회하고, 주문 아이템을 처리한 뒤, 전체 주문을 마지막에 한꺼번에 저장
     *
     *
     * 그러나?
     * 실제 포스트맨으로 실행해본 결과 (순서는 노션에 정리해둘 것)
     * stock 의 변경사항 업데이트는 가장 마지막에 이뤄졌다.
     * 락이 해제되기 전에 변경사항이 업데이트 되어야하는게 동시성문제해결의 포인트인데, 전혀 해결되지 않았다.
     *
     * 일단 딱봐도 안되는 상황이지만 테스트코드를 세팅하고 실행해보았는데 역시나였다.
     *
     *
     *
     */

    @Transactional
    public OrderResponse orderItems(OrderRequest request, CustomUserDetails userDetails) {

        List<OrderItemResponse> orderItemResponses = new ArrayList<>();

        // Order 생성 시에 넣어야할 user 엔티티를 꺼내온다.
        // 현재 oneToOne 양방향관계로 인한 Fetch.Lazy 비동작 문제때문에 패치조인으로 user 을 가져왔음
        User user = userRepository.findUserWithCartByUserId(userDetails.getUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        // OrderItem 를 생성할 때 필요한 Order 엔티티 준비
        Orders order = new Orders(user, ACCEPT, request.getDeliveryInfo());

        // 주문 실패시 선택 옵션 (전체 취소옵션이면 앞으로의 로직에서 예외를 던질 것임)
        OrderCancelOption orderCancelOption = Optional.ofNullable(request.getOrderCancelOption())
                .orElse(CANCEL_OUT_OF_STOCK_ONLY);


        // 주문요청 물품들에 대해 주문시도
        // 물품을 주문할 때 물품 개수가 많을 수록 db에 오가는 횟수가 많아지겠지만  그 모두에 락을 한번에 거는건.. 아닌 것 같아서, 하나씩 차례대로 처리 (맞는지는 모르겠다..)
        for (OrderItemRequest orderItemRequest : request.getOrderItemRequests()) {

            // MY_EXPECTED) 이 때 하나의 itemStock 에 대한 lock 획득, 해제가 되게끔 해야한다.
            // 각 주문시도에 대한 결과 dto 를 모은다.
            orderItemResponses.add(processOrderItem(order, orderCancelOption, orderItemRequest));
        }

        Orders savedOrder = orderRepository.save(order);
        return OrderResponse.of(savedOrder, request.getDeliveryInfo(), orderItemResponses);
    }

    private OrderItemResponse processOrderItem(Orders order, OrderCancelOption orderCancelOption, OrderItemRequest orderItemRequest) {

        OrderItemResponse orderItemResponse;
        RLock lock = redissonClient.getLock(
                String.valueOf(orderItemRequest.getItemStockId()));

        // 몇 초 동안 락 획득을 시도할 것인지, 몇초동안 점유할 것인지 설정
        try {
            boolean available = lock.tryLock(20, 1, TimeUnit.SECONDS);

            // 락 획득 실패시
            if (!available) {
                log.debug("fail-getLock ={}", lock.getName());
                return handleLockFailure(orderItemRequest, orderCancelOption);
            }

            // MY_EXPECTED) 락 걸릴 동안 작업 되야하는 부분
            orderItemResponse = orderItem(orderItemRequest, orderCancelOption, order);

        } catch (InterruptedException e) {
            throw new OrderException(UNPROCESSABLE_ORDER);
        } finally {
            lock.unlock();
        }
        return orderItemResponse;
    }


    // MY_EXPECTED) 상위 트랜잭션의 상태가 아닌 동시성에 대한 처리는 별도의 트랜잭션으로 동작을 해야한다.
    // orderLock 안에서 호출할 메서드
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OrderItemResponse orderItem(OrderItemRequest request,
                                       OrderCancelOption orderCancelOption,
                                       Orders order) {


        // itemStock 없다고 무조건 예외를 던질 상황이 아니라서 Optional 로 받았음
        // MY_EXPECTED) 새로운 별개의 트랜잭션에서 새롭게 엔티티를 꺼낸 상황 (해당 엔티티는 영속화된 상태)
        Optional<ItemStock> itemStockOptional = itemStockRepository
                .findItemStockByFetch(request.getItemStockId());

        if (itemStockOptional.isEmpty()) {
            return handleItemStockNotFound(request, orderCancelOption);
        }

        // 아이템 자체가 존재할 때
        ItemStock itemStock = itemStockOptional.get();

        // 주문 시도 + 주문결과 상태를 반환
        OrderResult orderResult = itemStock.orderItemStock(request.getQuantity());


        if (!SUCCESS.equals(orderResult)) {
            return handleOrderFailure(request, orderCancelOption, orderResult);
        }

        // 주문 성공 시 실제 주문아이템 데이터 생성 후 order 에 추가
        order.addOrderItem(itemStock, request.getQuantity(), itemStock.getItem().getPrice());

        // 주문 결과의 상태를 같이 넣어서 다시 dto 로 반환
        return OrderItemResponse.of(
                request.getCartItemId(),
                request.getQuantity(),
                itemStock,
                orderResult);

        // MY_EXPECTED) 이 메서드가 끝날 땐 변경감지로 ItemStock 의 변경사항이 db에 저장될 것으로 예상
    }



    private OrderItemResponse handleLockFailure(OrderItemRequest orderItemRequest,
                                                OrderCancelOption orderCancelOption) {

        // 전체 취소 옵션이라면 바로 예외를 던져버리고, 아니라면 dto 을 반환
        if (CANCEL_ENTIRE_ORDER.equals(orderCancelOption)) {
            throw new OrderException(UNPROCESSABLE_ORDER,
                    OrderItemResponse.of(orderItemRequest, INTERNAL_ERROR));
        }
        return OrderItemResponse.of(orderItemRequest, INTERNAL_ERROR);
    }


    private OrderItemResponse handleItemStockNotFound(OrderItemRequest request,
                                                      OrderCancelOption orderCancelOption) {
        if (CANCEL_ENTIRE_ORDER.equals(orderCancelOption)) {
            throw new OrderException(ErrorCode.NOT_FOUND_ITEM, OrderItemResponse.of(request, NOT_FOUND_ITEM));
        }
        return OrderItemResponse.of(request, NOT_FOUND_ITEM);
    }


    private OrderItemResponse handleOrderFailure(OrderItemRequest request,
                                                 OrderCancelOption orderCancelOption,
                                                 OrderResult orderResult) {
        if (CANCEL_ENTIRE_ORDER.equals(orderCancelOption)) {
            throw new OrderException(ITEM_UNAVAILABLE, OrderItemResponse.of(request, orderResult));
        }
        return OrderItemResponse.of(request, orderResult);
    }


}
