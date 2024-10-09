package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.type.CategoryName;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SellerItemResponse {


    private String nickname;

    private String name;

    private String imageUrl;

    private String description;

    private Integer price;

    private Integer stock;

    private ClothingSizeName sizeName;

    private CategoryName categoryName;

    private ItemStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;

    public SellerItemResponse(String nickname, String name, String imageUrl, String description, Integer price, Integer stock, ClothingSizeName sizeName, CategoryName categoryName, ItemStatus status, LocalDateTime expiredAt) {
        this.nickname = nickname;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.sizeName = sizeName;
        this.categoryName = categoryName;
        this.status = status;
        this.expiredAt = expiredAt;
    }

}
