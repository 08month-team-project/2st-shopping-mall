package com.example.shoppingmall.domain.cart.application;

import com.example.shoppingmall.domain.cart.dao.CartRepository;
import com.example.shoppingmall.domain.cart.domain.Cart;
import com.example.shoppingmall.domain.cart.domain.CartItem;
import com.example.shoppingmall.domain.cart.dto.AddCartItemRequest;
import com.example.shoppingmall.domain.item.dao.CartItemRepository;
import com.example.shoppingmall.domain.item.dao.ItemStockRepository;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.global.exception.ErrorCode;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

            // 장바구니에 동일한 아이템 존재하는지 확인
            // 존재한다면 기존 아이템에 수량만 변경, 없다면 새로 만들어서 추가
            Optional<CartItem> cartItemOptional = cartItemRepository
                    .findCartItem(cartOptional.get().getId(), itemStock.getId());

            if (cartItemOptional.isPresent()) {
                cartItemOptional.get().addQuantity(request.getQuantity());
            } else {
                cartItemRepository.save(
                        CartItem.of(cartOptional.get(),
                                itemStock.getItem(),
                                itemStock,
                                request.getQuantity()));
            }

        } else {
            // 만약 장바구니가 존재하지 않는다면 새로 생성하고 바로 아이템을 담는다.
            User user = userRepository.findById(userDetails.getUserId())
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
            user.addCart();
            user.getCart().addCartItem(itemStock, request.getQuantity());
        }


    }
}
