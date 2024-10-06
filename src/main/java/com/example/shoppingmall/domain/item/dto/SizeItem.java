package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SizeItem {

    private ClothingSizeName sizeName;

    public static SizeItem fromEntity(ClothingSize clothSize) {
        return SizeItem.builder()
                .sizeName(clothSize.getSizeName())
                .build();
    }
}
