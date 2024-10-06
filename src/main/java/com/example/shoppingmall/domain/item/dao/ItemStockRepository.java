package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ItemStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemStockRepository extends JpaRepository<ItemStock, Long> {

    @Query("select is from ItemStock is join fetch is.item i where is.id = :itemStockId")
    Optional<ItemStock> findItemStockWithItem(@Param("itemStockId") long itemStockId);
}

