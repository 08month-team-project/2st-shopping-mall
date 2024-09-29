package com.example.shoppingmall.domain.item.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
public class ItemImage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

    @Column(nullable = false)
    private String imageUrl;

    private boolean isThumbnail;
}
