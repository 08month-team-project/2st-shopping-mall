package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ItemImage;
import lombok.Getter;

@Getter
public class ItemImageResponse {

    private long imageUrlId;
    private String imageUrl;

    public ItemImageResponse(ItemImage image) {
        this.imageUrlId = image.getId();
        this.imageUrl = image.getImageUrl();
    }
}
