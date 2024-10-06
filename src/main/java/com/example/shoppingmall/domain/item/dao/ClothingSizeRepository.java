package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClothingSizeRepository extends JpaRepository<ClothingSize, Long> {
    Optional<ClothingSize> findBySizeName(ClothingSizeName SizeName);
}
