package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.order.type.OrderResult;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.shoppingmall.global.exception.ErrorCode.INVALID_STOCK;
import java.time.LocalDateTime;

import static com.example.shoppingmall.domain.item.type.ItemStatus.ALL_OUT_OF_STOCK;
import static com.example.shoppingmall.domain.order.type.OrderResult.*;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item_stock")
@Getter
@Entity
public class ItemStock {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_stock_id")
    private Long id;

    @Column(nullable = false)
    private Integer stock;

    @JoinColumn(name = "clothing_size_id")
    @ManyToOne(fetch = LAZY)
    private ClothingSize clothingSize;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

    public ItemStock(Item item, ClothingSize size, Integer stock) {
        this.item = item;
        this.clothingSize = size;
        this.stock = stock;
    }

    public static ItemStock of(Item item, ClothingSize size, Integer stock) {
        return new ItemStock(item, size, stock);
    }

    public void addStock(int stock) {
        this.stock += stock;
    }

    public void changeStock(int newStock) {

        this.stock -= newStock;

        if (this.stock < 0) {
            throw new ItemException(INVALID_STOCK);
        }
    }

    public OrderResult orderItemStock(int quantity) {

        if (ALL_OUT_OF_STOCK.equals(this.item.getStatus())) {
            return FAIL_SOLD_OUT;
        }
        if (item.getExpiredAt().isBefore(LocalDateTime.now())) {
            return FAIL_SALE_DATE_EXPIRATION;
        }
        if (this.stock - quantity < 0) {
            return STOCK_SHORTAGE;
        }

        this.stock -= quantity;
        return SUCCESS;
    }
}
