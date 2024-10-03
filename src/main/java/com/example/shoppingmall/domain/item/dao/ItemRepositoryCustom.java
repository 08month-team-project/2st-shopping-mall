package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositoryCustom {

    Page<Item> searchItems(Long categoryId,
                           String itemName,
                           StatusCondition statusCondition,
                           SortCondition sortCondition,
                           int pageNumber);
}
