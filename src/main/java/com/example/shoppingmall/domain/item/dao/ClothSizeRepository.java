package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ClothSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothSizeRepository extends JpaRepository<ClothSize, Long> {
}
