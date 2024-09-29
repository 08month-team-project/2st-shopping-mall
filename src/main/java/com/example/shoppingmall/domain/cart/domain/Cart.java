package com.example.shoppingmall.domain.cart.domain;

import com.example.shoppingmall.domain.user.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cart_id")
    private Long id;


    @JoinColumn(name = "user_id")
    @OneToOne(fetch = LAZY)
    private Users users;

    /* TODO 양방향 고려
    *   - user (oneToOne)
    *   - cart_item (oneToMany)
    */


}
