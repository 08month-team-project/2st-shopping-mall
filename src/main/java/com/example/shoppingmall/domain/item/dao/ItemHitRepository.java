package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ItemHit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemHitRepository extends JpaRepository<ItemHit, Long> {
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
}
