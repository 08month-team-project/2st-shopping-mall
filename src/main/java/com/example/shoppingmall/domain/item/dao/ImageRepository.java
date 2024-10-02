package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findAllByItemId(Long itemId);
}
