package com.example.shoppingmall.domain.my.dto;

import com.example.shoppingmall.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageRequest {
    private String name;
    private String email;
    private String phone;

    // 전송해야할 데이터는 이름,닉네임,폰번,이메일,성별,주소,소개글
    public MyPageRequest(User user) {
        name = user.getName();
        email = user.getEmail();
        phone = user.getPhoneNumber();
    }

}
