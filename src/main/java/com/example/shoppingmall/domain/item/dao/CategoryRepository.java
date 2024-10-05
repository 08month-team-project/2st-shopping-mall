package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.type.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
     Optional<Category> findByCategoryName(CategoryName categoryName);
}
