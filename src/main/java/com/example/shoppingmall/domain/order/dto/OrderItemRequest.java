package com.example.shoppingmall.domain.order.dto;

import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItemRequest {

    /* 본래 요청에서는 itemStockId, cartItemId, quantity 만 있어도 됐었으나,
     주문에 실패한 경우 어떤 물품에 대해 실패했는지 노출하기 위하여 넣었음 (다시 그대로 응답 dto에 넣기 위해서)
    */

    private Long itemStockId;
    private Long cartItemId; // null 이면 장바구니가 아닌 물품상세페이지에서 바로 주문한 것 (단독 주문)

    private Long itemId;
    private String itemName;
    private ClothingSizeName clothingSizeName;
    private int price;
    private int quantity; // 요청 수량
}
