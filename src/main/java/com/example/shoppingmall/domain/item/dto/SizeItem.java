package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ClothSize;
import com.example.shoppingmall.domain.item.type.ClothingSize;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SizeItem {

    private ClothingSize size;

    public static SizeItem fromEntity(ClothSize clothSize) {
        return SizeItem.builder()
                .size(clothSize.getSizeName())
                .build();
    }
}
