package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemStockRepository extends JpaRepository<ItemStock, Long> {
}
