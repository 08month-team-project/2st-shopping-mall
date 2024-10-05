package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.CategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long> {
}
