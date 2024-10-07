package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.type.CategoryName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryList {
    
    private CategoryName categoryName;

    public static CategoryList fromEntity(Category category) {
        return CategoryList.builder()
                .categoryName(category.getCategoryName())
                .build();
    };
    
}
