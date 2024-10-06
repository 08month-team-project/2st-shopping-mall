package com.example.shoppingmall.domain.cart.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
public class AddCartItemRequest {

    private long itemStockId;

    @Range(min = 1, max = 100, message = "상품담기: 1~100 개 허용")
    private int quantity;
}
