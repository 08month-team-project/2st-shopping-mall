package com.example.shoppingmall.domain.item.dto;

import com.example.shoppingmall.domain.item.type.CategoryName;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.example.shoppingmall.global.annotation.Censor;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RegisterRequest {

    @NotNull(message = "이미지 목록은 필수입니다.")
    @Size(min = 1, message = "최소 1개의 이미지 파일 필요합니다.")
    @JsonProperty("images_url")
    private List<@NotNull(message = "이미지 칸은 비어있을 수 없습니다.") String> imagesUrl;

    @Censor  // 비속어 필터링 추가
    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "카테고리는 필수입니다.")
    private CategoryName category;

    @NotNull(message = "재고 수량은 필수입니다.")
    @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
    private Integer stuck;

    @Censor  // 비속어 필터링 추가
    @Size(max = 500, message = "설명은 최대 500자 이내여야 합니다.")
    private String description;

    @Future(message = "만료일은 현재 시점 이후여야 합니다.")
    private LocalDateTime expiredAt;

    @NotNull(message = "싸이즈를 선택해주세요")
    @JsonProperty("size_name")
    private ClothingSizeName sizeName;

}
