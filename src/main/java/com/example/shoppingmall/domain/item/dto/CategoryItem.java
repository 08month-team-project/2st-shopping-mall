package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.type.CategoryName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryItem {
    
    private CategoryName categoryName;

    public static CategoryItem fromEntity(Category category) {
        return CategoryItem.builder()
                .categoryName(category.getCategoryName())
                .build();
    };
    
}
