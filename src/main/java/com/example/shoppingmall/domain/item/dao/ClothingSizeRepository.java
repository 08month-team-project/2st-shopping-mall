package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ClothingSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothingSizeRepository extends JpaRepository<ClothingSize, Long> {
}
