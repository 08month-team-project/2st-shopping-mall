package com.example.shoppingmall.domain.cart.api;

import com.example.shoppingmall.domain.cart.application.CartService;
import com.example.shoppingmall.domain.cart.dto.AddCartItemRequest;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController { // TODO 정말 만약에 시간이 남는다면, 미로그인 장바구니 이용 로직 추가..

    private final CartService cartService;


    @PostMapping
    public ResponseEntity<Void> addCartItem(
            @Valid @RequestBody AddCartItemRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        cartService.addCartItem(userDetails, request);
        return ResponseEntity.ok().build();
    }
}
