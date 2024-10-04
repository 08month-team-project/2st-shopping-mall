package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.type.CategoryName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", nullable = false)
    @Enumerated(STRING)
    private CategoryName categoryName;

    public Category(CategoryName categoryName) {
        this.categoryName = categoryName;
    }

    /** 양방향 고려
     * - category_item
     */
}
