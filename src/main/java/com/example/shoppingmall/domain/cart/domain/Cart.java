package com.example.shoppingmall.domain.cart.domain;

import com.example.shoppingmall.domain.item.domain.Item;
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

    /**
     * db에서 Cart를 찾아올때, 처음부터 cartItems 와 조인 패치 한 뒤에,
     * Cart 내부에서 이미 존재하는 아이템인지를 검증하는 게 조금 더 객체지향적? 같다고 생각이 들지만,
     * cartItems 가 몇 개 일지도 모르는 점 등 성능 문제가 생길 것 같아서, 서비스에 맡기었다.
     */
    public void addCartItem(Item item, ItemStock itemStock, int quantity) {

        CartItem cartItem = CartItem.of(this, item, itemStock);
        cartItem.addQuantity(quantity);

        cartItems.add(cartItem);
    }


}
