package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ItemImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ItemImageResponse {

    private long imageUrlId;
    private String imageUrl;

    public static ItemImageResponse from(ItemImage image) {
        return ItemImageResponse.builder()
                .imageUrlId(image.getId())
                .imageUrl(image.getImageUrl())
                .build();
    }
}
