package com.example.shoppingmall.domain.order.dto;

import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.example.shoppingmall.domain.order.type.OrderResult;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class OrderItemResponse {

    private Long cartItemId; // null 이 아니면서 결과가 주문성공이면 프론트에서 해당 id를 가지고 삭제 요청 날리게끔

    private Long itemId;

    private String itemName;

    private Long itemStockId;

    private ClothingSizeName clothingSizeName;

    private int price;

    private long orderQuantity;

    private OrderResult orderResult; // 주문 성공 여부


    // 주문 성공일 경우
    public static OrderItemResponse of(Long cartItemId,
                                       long orderQuantity,
                                       ItemStock itemStock,
                                       OrderResult orderResult) {

        return OrderItemResponse.builder()
                .cartItemId(cartItemId)
                .itemId(itemStock.getItem().getId())
                .itemName(itemStock.getItem().getName())
                .itemStockId(itemStock.getId())
                .clothingSizeName(itemStock.getClothingSize().getSizeName())
                .price(itemStock.getItem().getPrice())
                .orderQuantity(orderQuantity)
                .orderResult(orderResult)
                .build();
    }


    // OrderCancelOption.CANCEL_OUT_OF_STOCK_ONLY 일 경우
    public static OrderItemResponse of(OrderItemRequest request,
                                       OrderResult orderResult) {

        return OrderItemResponse.builder()
                .cartItemId(request.getCartItemId())
                .itemId(request.getItemId())
                .itemName(request.getItemName())
                .itemStockId(request.getItemStockId())
                .clothingSizeName(request.getClothingSizeName())
                .price(request.getPrice())
                .orderQuantity(request.getQuantity())
                .orderResult(orderResult)
                .build();
    }
}
