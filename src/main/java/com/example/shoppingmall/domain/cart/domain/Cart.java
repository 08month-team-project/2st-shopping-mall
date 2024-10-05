package com.example.shoppingmall.domain.cart.domain;

import com.example.shoppingmall.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cart_id")
    private Long id;


    @JoinColumn(name = "user_id")
    @OneToOne(fetch = LAZY)
    private User user;


    public Cart(User user) {
        this.user = user;
    }




}
