package com.example.shoppingmall.domain.user.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.user.type.Gender;
import com.example.shoppingmall.domain.user.type.UserRole;
import com.example.shoppingmall.domain.user.type.UserStatus;
import com.example.shoppingmall.global.security.dto.UserDetailsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String profileImageUrl;


    /* TODO 양방향 고려
        - cart (oneToOne)

        - order (oneToMany)
        - item (oneToMany)
     */

    @PrePersist
    public void prePersist() {
        role = UserRole.CUSTOMER;
        status = UserStatus.ACTIVE;
    }

    public UserDetailsDTO toUserDetailsDTO(){
        return UserDetailsDTO.builder()
                .email(email)
                .password(password)
                .role(role.name()).build();
    }

}
