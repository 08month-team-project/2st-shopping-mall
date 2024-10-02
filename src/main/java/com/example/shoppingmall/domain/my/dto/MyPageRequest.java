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

    public MyPageRequest(User user) {
        name = user.getName();
        email = user.getEmail();
        phone = user.getPhoneNumber();
    }

}
