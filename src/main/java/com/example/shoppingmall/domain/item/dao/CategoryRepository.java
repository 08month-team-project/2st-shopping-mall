package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
