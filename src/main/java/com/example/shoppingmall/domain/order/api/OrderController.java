package com.example.shoppingmall.domain.order.api;

import com.example.shoppingmall.domain.order.application.OrderServiceV1;
import com.example.shoppingmall.domain.order.dto.OrderRequest;
import com.example.shoppingmall.domain.order.dto.OrderResponse;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceV1 orderServiceV1;

    /**
     * 장바구니 자체에서 여러종류의 물품을 선택해서 주문하는 상황 (+ 상세페이지에서 한 종류의 물품을 주문하는 상황)
     */
    @PostMapping
    public ResponseEntity<OrderResponse> orderItems(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(orderServiceV1.orderItems(request, userDetails));
    }
}
