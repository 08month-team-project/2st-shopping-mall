package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@AllArgsConstructor
@Getter
public class ItemSizeStockResponse {

    private long itemStockId;
    private long clothingSizeId;
    private ClothingSizeName clothingSizeName;
    private int stock;

    public static ItemSizeStockResponse from(ItemStock itemStock) {
        return ItemSizeStockResponse.builder()
                .itemStockId(itemStock.getId())
                .clothingSizeId(itemStock.getClothingSize().getId())
                .clothingSizeName(itemStock.getClothingSize().getSizeName())
                .stock(itemStock.getStock())
                .build();
    }
}
