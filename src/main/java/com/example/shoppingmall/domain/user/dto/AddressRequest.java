package com.example.shoppingmall.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    @NotBlank(message = "주소를 입력해주세요.")
    private String city;
    @NotBlank(message = "우편번호를 입력해주세요.")
    private String zipcode;
}
