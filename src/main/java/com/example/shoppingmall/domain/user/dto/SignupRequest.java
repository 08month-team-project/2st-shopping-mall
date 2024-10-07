package com.example.shoppingmall.domain.user.dto;

import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.global.annotation.BadWordFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
            message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @BadWordFilter // 다른 패키지에서도 동작 하는지 확인
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]{8,20}$",
            message = "비밀번호는 영문자, 숫자 조합 8자 이상, 20자 이하를 사용하세요.")
    private String password;

    @NotNull(message = "성별을 선택하세요.")
    private Gender gender;

    @JsonProperty("phone_number")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private String phoneNumber;

    @Valid
    private AddressRequest address;

    public User dtoToEntity(String encodedPwd){
        return User.builder()
                .email(email).name(name).nickname(nickname)
                .password(encodedPwd).gender(gender).phoneNumber(phoneNumber)
                .address(Address.builder()
                        .city(address.getCity())
                        .zipcode(address.getZipcode()).build())
                .build();
    }
}
