package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findAllByItemId(Long itemId);
}
