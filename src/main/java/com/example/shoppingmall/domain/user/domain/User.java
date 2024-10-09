package com.example.shoppingmall.domain.user.domain;

import com.example.shoppingmall.domain.cart.domain.Cart;
import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.user.dto.MyPageRequest;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.domain.user.type.UserRole;
import com.example.shoppingmall.domain.user.type.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
public class User extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, fetch = LAZY)
    private Cart cart;

    private String comment;


    @PrePersist
    public void prePersist() {
        role = UserRole.CUSTOMER;
        status = UserStatus.ACTIVE;
        addCart();
    }

    // 굳이 public 메서드로 한 이유
    // 모종의 이유로 cart 가 존재하지 않을 때, 서비스에서 새로 만들 수 있게 하기 위함
    public void addCart() {
        cart = new Cart(this);
    }

    public void deleteUser(){
        status = UserStatus.WITHDRAWAL;
    }

    public void changeRoleSeller(){
        role = UserRole.SELLER;
    }

    public void updateProfileImageUrl(String imageUrl){
        this.profileImageUrl = imageUrl;
    }
    public void updateProfile(MyPageRequest myPageRequest){
        this.email = myPageRequest.getEmail();
        this.name = myPageRequest.getName();
        this.nickname = myPageRequest.getNickname();
        this.password = myPageRequest.getPassword();
        this.gender = myPageRequest.getGender();
        this.phoneNumber = myPageRequest.getPhone();
        this.address = myPageRequest.getAddress();
        this.comment = myPageRequest.getComment();
    }

}
