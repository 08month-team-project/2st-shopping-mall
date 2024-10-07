package com.example.shoppingmall.domain.item.domain;

import com.example.shoppingmall.domain.item.type.CategoryName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryItem> categoryItems = new ArrayList<>();

    public Category(CategoryName categoryName) {
        this.categoryName = categoryName;
    }

    public void addItem(Item item) {
        CategoryItem categoryItem = CategoryItem.of(this, item);
        categoryItems.add(categoryItem);
    }

    /** 양방향 고려
     * - category_item
     */
}
