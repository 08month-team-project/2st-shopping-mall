package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.Item;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;


@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ItemResponse {

    private long itemId;
    private String itemName;
    private int price;
    private String thumbnailUrl;
    private long hits;


    public ItemResponse (Item item) {
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.price = item.getPrice();
        this.thumbnailUrl = item.getThumbnailUrl();
        this.hits = item.getHitCount();
    }
}
