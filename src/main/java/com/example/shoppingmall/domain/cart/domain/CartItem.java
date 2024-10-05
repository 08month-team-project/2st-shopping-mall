package com.example.shoppingmall.domain.cart.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.shoppingmall.global.exception.ErrorCode.CART_QUANTITY_EXCEEDS_STOCK;
import static com.example.shoppingmall.global.exception.ErrorCode.PRODUCT_NOT_FOR_SALE;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
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

    @JoinColumn(name = "item_stock_id")
    @ManyToOne(fetch = LAZY)
    private ItemStock itemStock;

    private int quantity;

    public static CartItem of(Cart cart, Item item, ItemStock itemStock, int quantity) {

        if (quantity == 0) quantity = 1;

        // 물품에 속하는 모든 옵션(사이즈 등) 의 재고가 없을 때 or 만료일자
        if (item.getStatus() == ItemStatus.OUT_OF_STOCK ||
                item.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ItemException(PRODUCT_NOT_FOR_SALE);
        }

        // 현재 재고보다 많은 수량을 담을 수 없음
        if (itemStock.getStock() < quantity) {
            throw new ItemException(CART_QUANTITY_EXCEEDS_STOCK);
        }

        return CartItem.builder()
                .cart(cart)
                .item(item)
                .itemStock(itemStock)
                .quantity(quantity)
                .build();
    }

    // 장바구기 담기 (장바구니 수정기능 X)
    public void addQuantity(int quantity) {
        if (quantity < 1) quantity = 1;

        this.quantity += quantity;
    }

}
