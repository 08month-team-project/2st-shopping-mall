package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateItemRequest {

    @NotNull(message = "재고 수량은 필수입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    private Integer stuck;

    @NotNull(message = "싸이즈를 선택해주세요")
    @JsonProperty("size_name")
    private ClothingSizeName sizeName;

}
