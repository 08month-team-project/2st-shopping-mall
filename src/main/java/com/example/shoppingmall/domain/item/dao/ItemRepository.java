package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
