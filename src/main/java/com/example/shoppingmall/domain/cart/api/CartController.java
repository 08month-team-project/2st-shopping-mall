package com.example.shoppingmall.domain.cart.api;

import com.example.shoppingmall.domain.cart.application.CartService;
import com.example.shoppingmall.domain.cart.dto.AddCartItemRequest;
import com.example.shoppingmall.domain.item.dto.CartItemResponse;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"},
        allowCredentials = "true",maxAge = 3600,
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PATCH,RequestMethod.PUT,RequestMethod.OPTIONS},
        exposedHeaders = {"Authorization","Content-Type"})
@RequestMapping("/carts")
public class CartController { // TODO 정말 만약에 시간이 남는다면, 미로그인 장바구니 이용 로직 추가..

    private final CartService cartService;
    @Operation(summary = "장바구니 담기")
    @PostMapping
    public ResponseEntity<Void> addCartItem(
            @Valid @RequestBody AddCartItemRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        cartService.addCartItem(userDetails, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 조회")
    @GetMapping
    public ResponseEntity<Slice<CartItemResponse>> getMyCartItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "page", defaultValue = "0") int pageNumber) {

        return ResponseEntity.ok(cartService.getMyCartItems(userDetails, pageNumber));
    }

    @Operation(summary = "장바구니 물품내역 수정")
    @PatchMapping("/items/{cart_item_id}")
    public ResponseEntity<Void> modifyCartItemQuantity(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable("cart_item_id") long cartItemId,
            @RequestParam("quantity") int quantity) {

        cartService.modifyCartItemQuantity(customUserDetails, cartItemId, quantity);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "장바구니 물품 삭제")
    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteCartItems(
            @RequestParam("cart_item_id") List<Long> cartItemIdList,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        cartService.deleteCartItems(cartItemIdList, userDetails);
        return ResponseEntity.ok().build();
    }

}
