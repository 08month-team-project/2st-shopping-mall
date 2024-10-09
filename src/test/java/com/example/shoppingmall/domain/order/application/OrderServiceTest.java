package com.example.shoppingmall.domain.order.application;

import com.example.shoppingmall.domain.item.dao.ClothingSizeRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.dao.ItemStockRepository;
import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.example.shoppingmall.domain.order.dto.DeliveryInfo;
import com.example.shoppingmall.domain.order.dto.OrderItemRequest;
import com.example.shoppingmall.domain.order.dto.OrderRequest;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.global.exception.ErrorCode;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import com.example.shoppingmall.global.security.dto.UserDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.shoppingmall.domain.order.type.OrderCancelOption.CANCEL_OUT_OF_STOCK_ONLY;
import static org.assertj.core.api.Assertions.assertThat;

@Rollback(value = false)
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderServiceV1 orderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClothingSizeRepository clothingSizeRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemStockRepository itemStockRepository;



    private User user;

    private CustomUserDetails userDetails;

    private List<Item> items;

    private OrderRequest orderRequest;

    private List<OrderItemRequest> orderItemRequests;

    private List<ClothingSize> clothingSizes;

    private List<ItemStock> itemStocks;


    @BeforeEach
    void init() {

        // 유저
        Address address = Address.builder()
                .city("서울 특별시 강남구 강남대로 123")
                .zipcode("1101").build();
        user = User.builder()
                .email("willow@gmail.com")
                .name("유")
                .nickname("유")
                .password("mypass1234")
                .gender(Gender.MALE)
                .phoneNumber("010-1234-1234")
                .address(address).build();

        user = userRepository.save(user);


        // 옷 사이즈
        clothingSizes = new ArrayList<>();
        for (ClothingSizeName value : ClothingSizeName.values()) {
            clothingSizes.add(new ClothingSize(value));
        }
        clothingSizes = clothingSizeRepository.saveAll(clothingSizes);


        // =======================
        items = new ArrayList<>();

        Item item = Item.builder()
                .name("상품1")
                .price(10000)
                .description("상품1은 10000원 입니다")
                .expiredAt(LocalDateTime.now().plusDays(10))
                .thumbnailUrl("상품1의 썸네일")
                .status(ItemStatus.IN_STOCK)
                .user(user)
                .build();
        item.addItemStock(clothingSizes.get(0), 100);
//        item.addItemStock(clothingSizes.get(1), 100);
//        item.addItemStock(clothingSizes.get(2), 100);
//        item.addItemStock(clothingSizes.get(3), 100);
//        item.addItemStock(clothingSizes.get(4), 100);
        items.add(item);

        items = itemRepository.saveAll(items);

        userDetails = new CustomUserDetails(UserDetailsDTO.from(user));


        // ===========================
        Item item0 = items.get(0);

        itemStocks = new ArrayList<>();
        itemStocks.add(item0.getStocks().get(0));
//        itemStocks.add(item0.getStocks().get(1));

// ==========================
        orderItemRequests = new ArrayList<>();

        orderItemRequests.add(OrderItemRequest.builder().
                itemStockId(itemStocks.get(0).getId())
                .cartItemId(null)
                .itemId(item0.getId())
                .itemName(item0.getName())
                .clothingSizeName(itemStocks.get(0).getClothingSize().getSizeName())
                .price(item0.getPrice())
                .quantity(1)
                .build());

//        orderItemRequests.add(OrderItemRequest.builder().
//                itemStockId(itemStocks.get(1).getId())
//                .cartItemId(null)
//                .itemId(item0.getId())
//                .itemName(item0.getName())
//                .clothingSizeName(itemStocks.get(1).getClothingSize().getSizeName())
//                .price(item0.getPrice())
//                .quantity(1)
//                .build());


        orderRequest = OrderRequest.builder()
                .orderCancelOption(CANCEL_OUT_OF_STOCK_ONLY)
                .deliveryInfo(new DeliveryInfo("수령인", "서울시", "0000"))
                .orderItemRequests(orderItemRequests)
                .build();


    }

    @Test
    void testConcurrentOrderRequests() throws InterruptedException {
        int threadCount = 50; // 동시에 주문을 요청할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);


        // 동시 주문 요청 시뮬레이션
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    orderService.orderItems(orderRequest, userDetails);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 끝날 때까지 대기
        executorService.shutdown();

        // 여기에 주문 결과에 대한 검증 로직 추가 가능
        ItemStock itemStock0 = itemStockRepository.findById(itemStocks.get(0).getId())
                .orElseThrow(() -> new ItemException(ErrorCode.NOT_FOUND_ITEM));

//        ItemStock itemStock1 = itemStockRepository.findById(itemStocks.get(1).getId())
//                .orElseThrow(() -> new ItemException(ErrorCode.NOT_FOUND_ITEM));


        assertThat(itemStock0.getStock()).isEqualTo(50);
    }


}