package com.example.shoppingmall.domain.item.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category_item")
@Getter
@Entity
public class CategoryItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_name_id")
    private Long id;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = LAZY)
    private Category category;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = LAZY)
    private Item item;

    public CategoryItem(Category category, Item item) {
        this.category = category;
        this.item = item;
    }
}
