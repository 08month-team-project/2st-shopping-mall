package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.item.type.ClothingSize;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StockResponse {

    private ClothingSize size;
    private int stock;

    public StockResponse(ItemStock stock) {
        this.size = stock.getSize();
        this.stock = stock.getStock();
    }
}
