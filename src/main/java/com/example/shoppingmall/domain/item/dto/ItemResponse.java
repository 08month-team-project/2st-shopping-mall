package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.type.CategoryName;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ItemResponse {

    private long itemId;
    private String itemName;
    private int price;
    private String thumbnailUrl;
    private long hits;
    private ItemStatus status;

    private long categoryId;
    private CategoryName categoryName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

}
