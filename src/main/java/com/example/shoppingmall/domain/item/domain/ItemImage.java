package com.example.shoppingmall.domain.item.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class ItemImage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    public ItemImage(Item item, String imageUrl) {
        this.item = item;
        this.imageUrl = imageUrl;
    }

    public ItemImage(Long imageId, Item item, String imageUrl) {
        this.id = imageId;
        this.item = item;
        this.imageUrl = imageUrl;
    }

    public static ItemImage of(Item item, String imageUrl) {
       return new ItemImage(item,imageUrl);
    }
}
