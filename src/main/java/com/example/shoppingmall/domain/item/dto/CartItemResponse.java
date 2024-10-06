package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.cart.domain.CartItem;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class CartItemResponse {

    // [cart_item]
    private Long cartItemId;
    private int quantity;

    // [item]
    private Long itemId;
    private String itemName;
    private int itemPrice;
    private ItemStatus itemStatus;
    private String thumbnailUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;

    // [item_stock]
    private Long itemStockId;
    private int currentStock;

    // [clothing_size]
    private Long clothingSizeId;
    private ClothingSizeName sizeName;

    public static CartItemResponse from(CartItem cartItem) { // 패치조인해온 CartItem
        return CartItemResponse.builder()
                .cartItemId(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .itemId(cartItem.getItem().getId())
                .itemName(cartItem.getItem().getName())
                .itemPrice(cartItem.getItem().getPrice())
                .itemStatus(cartItem.getItem().getStatus())
                .thumbnailUrl(cartItem.getItem().getThumbnailUrl())
                .expiredAt(cartItem.getItem().getExpiredAt())
                .itemStockId(cartItem.getItemStock().getId())
                .currentStock(cartItem.getItemStock().getStock())
                .clothingSizeId(cartItem.getItemStock().getClothingSize().getId())
                .sizeName(cartItem.getItemStock().getClothingSize().getSizeName())
                .build();
    }
}
