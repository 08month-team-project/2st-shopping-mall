package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryItem {
    
    private String categoryName;

    public static CategoryItem fromEntity(Category category) {
        return CategoryItem.builder()
                .categoryName(category.getName())
                .build();
    };
    
}
