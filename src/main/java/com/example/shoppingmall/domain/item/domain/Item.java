package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Item extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private User user;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_price", nullable = false)
    private Integer price;

    @OneToMany(mappedBy = "item")
    private List<ItemImage> images = new ArrayList<>();

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(nullable = false, name = "expired_at")
    private LocalDateTime expiredAt;

    // TODO 조회 수 구현하게 될 때 생각해볼 예정
    @Column(nullable = false, name = "hit_count")
    private Long hitCount;


    /* TODO 양방향 고려
     *  - cart_item
     *  - order_item
     *  - item_stock
     *  - item_category
     *  - item_image
     */
}
