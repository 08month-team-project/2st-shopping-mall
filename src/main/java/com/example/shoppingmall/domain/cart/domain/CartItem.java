package com.example.shoppingmall.domain.cart.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.item.domain.Item;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
public class CartItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @JoinColumn(name = "cart_id")
    @ManyToOne(fetch = LAZY)
    private Cart cart;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

    private int quantity;
}
