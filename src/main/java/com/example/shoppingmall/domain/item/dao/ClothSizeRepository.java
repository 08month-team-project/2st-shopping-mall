package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ClothSize;
import com.example.shoppingmall.domain.item.type.ClothingSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothSizeRepository extends JpaRepository<ClothSize, Long> {
    Optional<ClothSize> findBySizeName(ClothingSize size);
}
