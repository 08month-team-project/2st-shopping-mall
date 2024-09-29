package com.example.shoppingmall.domain.order.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.order.type.OrderStatus;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Users users;

    @Enumerated(value = STRING)
    private OrderStatus status;

    @Embedded
    private Address address;


    /* TODO 양방향 고려
    *   - order_item
    */
}
