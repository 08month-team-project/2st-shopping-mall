package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.type.ClothingSize;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
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

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

    @JoinColumn(name = "cloth_size_id")
    @ManyToOne(fetch = LAZY)
    private ClothSize clothSize;

    @Column(nullable = false)
    private Integer stock;


    public ItemStock(Item item, Integer stock) {
        this.item = item;
        this.stock = stock;
    }

    public void addStock(int stock) {
        this.stock += stock;
    }

    //
    public ItemStock(Integer stock, ClothSize clothSize) {
        this.stock = stock;
        this.clothSize = clothSize;
    }

    public static ItemStock of(Integer stock, ClothSize clothSize) {
        return new ItemStock(stock,clothSize);
    }
}
