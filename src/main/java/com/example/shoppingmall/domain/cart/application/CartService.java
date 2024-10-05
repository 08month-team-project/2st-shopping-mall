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
import com.example.shoppingmall.global.security.dto.UserDetailsDTO;
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


    public void addCartItem(UserDetailsDTO userDetails, AddCartItemRequest request) {

        // 인증객체를 통한 유저아이디에 속한 장바구니를 가져온다.
        Optional<Cart> cartOptional = cartRepository.findByUserId(userDetails.getUserId());
        Cart cart;

        // 만약 장바구니가 존재하지 않는다면 새로 생성
        if (cartOptional.isPresent()) {
            cart = cartOptional.get();
        } else {
            User user = userRepository.findById(userDetails.getUserId())
                    .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
            cart = new Cart(user);
        }

        ItemStock itemStock = itemStockRepository.findItemStockWithItem(request.getItemStockId())
                .orElseThrow(() -> new ItemException(ErrorCode.NOT_FOUND_ITEM));

        cartItemRepository.save(
                CartItem.of(cart, itemStock.getItem(), itemStock, request.getQuantity()));
    }
}
