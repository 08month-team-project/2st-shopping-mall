package com.example.shoppingmall.domain.user.dto;

import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponse {
    private String name;
    private String nickName;
    private String email;
    private String phone;
    private Gender gender;
    private Address address;
    private String comment;
    private String imageUrl;

    // 전송해야할 데이터는 이름,닉네임,폰번,이메일,성별,주소,소개글
    public MyPageResponse(User user) {
        this.name = user.getName();
        this.nickName = user.getNickname();
        this.email = user.getEmail();
        this.phone = user.getPhoneNumber();
        this.gender = user.getGender();
        this.address = user.getAddress();
        this.comment = user.getComment();
        this.imageUrl = user.getProfileImageUrl();
    }

}
