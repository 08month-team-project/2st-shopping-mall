package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.dto.SellerItemResponse;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepositorySellerCustom {

    Page<SellerItemResponse> findAllByUserAndStatus(Long userId, ItemStatus status, Pageable adjustedPageable);
}
