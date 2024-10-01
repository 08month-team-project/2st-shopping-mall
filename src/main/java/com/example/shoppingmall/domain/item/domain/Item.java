package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.user.domain.Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.*;

@Getter
@Entity
public class Item extends BaseTimeEntity {

    // TODO 카테고리

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private Users user;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_price", nullable = false)
    private Integer price;

    @OneToMany(mappedBy = "item")
    private List<ItemImage> images = new ArrayList<>();

    private int stock;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "thumbnail_url", nullable = false)
    private String thumbnailUrl;

    // TODO 조회 수 구현하게 될 때 생각해볼 예정
    private long hitCount;


    /* TODO 양방향 고려
     *  - cate_item
     *  - order_item
     */
}
