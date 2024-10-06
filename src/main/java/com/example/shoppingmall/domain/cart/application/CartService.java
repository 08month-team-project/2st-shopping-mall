package com.example.shoppingmall.domain.cart.application;

import com.example.shoppingmall.domain.cart.dao.CartRepository;
import com.example.shoppingmall.domain.cart.domain.Cart;
import com.example.shoppingmall.domain.cart.domain.CartItem;
import com.example.shoppingmall.domain.cart.dto.AddCartItemRequest;
import com.example.shoppingmall.domain.cart.excepction.CartException;
import com.example.shoppingmall.domain.item.dao.CartItemRepository;
import com.example.shoppingmall.domain.item.dao.ItemStockRepository;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.item.dto.CartItemResponse;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.global.exception.ErrorCode;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final ItemStockRepository itemStockRepository;
    private final CartItemRepository cartItemRepository;


    public void addCartItem(CustomUserDetails userDetails, AddCartItemRequest request) {

        // 담고싶은 물품정보를 가져온다.
        ItemStock itemStock = itemStockRepository.findItemStockWithItem(request.getItemStockId())
                .orElseThrow(() -> new ItemException(ErrorCode.NOT_FOUND_ITEM));


        // 인증객체로 가져온 유저아이디에 속한 장바구니를 가져온다.
        Optional<Cart> cartOptional = cartRepository.findByUserId(userDetails.getUserId());

        // 장바구니가 존재하면 User 엔티티를 찾아올 필요가 없다.
        if (cartOptional.isPresent()) {
            Cart cart = cartOptional.get();

            // 장바구니에 동일한 아이템 존재하는지 확인
            Optional<CartItem> cartItemOptional = cartItemRepository
                    .findCartItemByFetch(cart.getId(), itemStock.getId());

            // 이미 담았던 물품이면 수량만 변경
            if (cartItemOptional.isPresent()) {
                cartItemOptional.get().addQuantity(request.getQuantity());

            } else { // 담은 적이 없다면 엔티티를 새로 만들어서 추가
                cart.addCartItem(itemStock.getItem(), itemStock, request.getQuantity());
            }

        } else {
            // 만약 장바구니가 존재하지 않는다면 새로 생성하고 바로 아이템을 담는다.
            User user = userRepository.findById(userDetails.getUserId())
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
            user.addCart();
            user.getCart()
                    .addCartItem(itemStock.getItem(), itemStock, request.getQuantity());
        }

    }


    /**
     * 검색때와 마찬가지로 Pageable 로 받지 않았습니다.
     * 현재는 잘못된 값이 들어왔을 때
     * 서비스나 커스텀레파지토리에서 처리해서 새로운 PageRequest 를 만들어서 사용하는 방식을 취했지만,
     * 다음 프로젝트 때 기회가 된다면
     * Pageable 에 관련한 커스텀 예외 처리 클래스를 따로 만들어보는 방식으로 해보겠습니다.
     */
    public Slice<CartItemResponse> getMyCartItems(CustomUserDetails userDetails, int pageNumber) {

        Long cartId = cartRepository.findCartId(userDetails.getUserId());
        if(cartId == null || cartId == 0){
            return new SliceImpl<>(new ArrayList<>(), PageRequest.ofSize(0), false);
        }
        if(--pageNumber < 0)  pageNumber = 0;
        System.out.println("pageNumber: " +  pageNumber);

        return cartItemRepository.findMyCartItems(cartId, cartPageable(pageNumber))
                .map(CartItemResponse::from);
    }

    private Pageable cartPageable(int pageNumber) {
        return PageRequest.of(pageNumber, 20, Sort.Direction.DESC, "createdAt");
    }
}
