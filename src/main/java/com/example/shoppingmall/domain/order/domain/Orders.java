package com.example.shoppingmall.domain.order.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.order.dto.DeliveryInfo;
import com.example.shoppingmall.domain.order.type.OrderStatus;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class Orders extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    @Enumerated(value = STRING)
    private OrderStatus status;

    @Embedded
    private Address address;

    private String receiver;

    @OneToMany(mappedBy = "order", cascade = PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Orders(User user, OrderStatus status, DeliveryInfo deliveryInfo) {
        this.user = user;
        this.status = status;
        this.address = new Address(deliveryInfo.getCity(), deliveryInfo.getZipcode());
        this.receiver = deliveryInfo.getReceiver();
    }

    public void addOrderItem(ItemStock itemStock, int quantity, int price) {
        orderItems.add(new OrderItem(this, itemStock, quantity, price));
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }
}
