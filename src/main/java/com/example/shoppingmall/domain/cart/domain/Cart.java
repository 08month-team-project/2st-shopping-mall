package com.example.shoppingmall.domain.cart.domain;

import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart(User user) {
        this.user = user;
    }


    public void addCartItem(ItemStock itemStock, int quantity) {
        cartItems.add(CartItem.of(this, itemStock.getItem(), itemStock, quantity));
    }


}
