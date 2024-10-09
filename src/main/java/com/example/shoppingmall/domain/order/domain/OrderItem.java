package com.example.shoppingmall.domain.order.domain;

import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    private Long itemStockId; // 일단 연관관계 안 맺고 아이디값만 넣었음

    public OrderItem(Orders order, ItemStock itemStock, Integer quantity, Integer price) {
        this.order = order;
        this.item = itemStock.getItem();
        this.itemStockId = itemStock.getId();
        this.quantity = quantity;
        this.price = price;
    }
}
