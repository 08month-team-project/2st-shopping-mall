package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.excepction.ItemException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.shoppingmall.global.exception.ErrorCode.INVALID_STOCK;
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

    public void addStock(int stock) {
        this.stock += stock;
    }

    public void changeStock(int newStock) {

        this.stock -= newStock;

        if (this.stock < 0) {
            throw new ItemException(INVALID_STOCK);
        }
    }
}
