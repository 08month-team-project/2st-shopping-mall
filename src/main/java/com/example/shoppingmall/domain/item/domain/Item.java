package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.common.BaseTimeEntity;
import com.example.shoppingmall.domain.item.type.ClothingSize;
import com.example.shoppingmall.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@AllArgsConstructor
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

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemStock> stocks = new ArrayList<>();

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(nullable = false, name = "expired_at")
    private LocalDateTime expiredAt;


    // TODO 조회 수 구현하게 될 때 생각해볼 예정
    @Column(nullable = false, name = "hit_count")
    private Long hitCount;

    @Column(nullable = false)
    private String description;


    /**
     * 이미 존재하는 옵션의 재고 수정 X
     * 새로운 옵션 자체를 추가
     */
    public void addStockOption(ClothingSize size, int stock) {

        for (ItemStock itemStock : stocks) {
            if(itemStock!= null && itemStock.getSize().equals(size)){
                itemStock.addStock(stock);
                break;
            }
        }
        stocks.add(new ItemStock(this, size, stock));
    }

    public void addImage(String imageUrl) {
        images.add(new ItemImage(this, imageUrl));
    }

    /* TODO 양방향 고려
     *  - cart_item
     *  - order_item
     *  - item_category
     *
     *  - item_stock
     *  - item_image
     */
}
