package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.type.ClothingSize;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
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

    @Column(nullable = false)
    @Enumerated(STRING)
    private ClothingSize size;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

}
