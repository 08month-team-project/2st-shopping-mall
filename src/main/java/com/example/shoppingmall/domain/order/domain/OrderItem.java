package com.example.shoppingmall.domain.order.domain;

import com.example.shoppingmall.domain.item.domain.Item;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = LAZY)
    private Orders order;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

    private int quantity;

    private int price;
}
