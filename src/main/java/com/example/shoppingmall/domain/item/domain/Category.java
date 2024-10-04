package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.type.CategoryName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false)
    private CategoryName name;

    /** 양방향 고려
     * - category_item
     */

    private Category(CategoryName name) {
        this.name = name;
    }
    // 정적 팩토리 메서드
    public static Category of(CategoryName name) {
        return new Category(name);
    }
}
