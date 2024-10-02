package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ItemImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemImageResponse {

    private long imageUrlId;
    private String imageUrl;

    public ItemImageResponse(ItemImage image) {
        this.imageUrlId = image.getId();
        this.imageUrl = image.getImageUrl();
    }
}
