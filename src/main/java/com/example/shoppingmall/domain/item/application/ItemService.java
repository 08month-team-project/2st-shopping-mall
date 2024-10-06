package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.ImageRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.dto.ItemResponse;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;


//    @Transactional(readOnly = true)
//    public ItemDetailResponse getItemDetail(long itemId) {
//
//        Item item = itemRepository.findItemAndStockAndSeller(itemId)
//                .orElseThrow(() -> new ItemException(NOT_FOUND_ITEM));
//
//        return new ItemDetailResponse(item, imageRepository.findAllByItemId(item.getId()));
//    }


    @Transactional(readOnly = true)
    public Page<ItemResponse> searchItems(Long categoryId,
                                          String itemName,
                                          StatusCondition statusCondition,
                                          SortCondition sortCondition,
                                          Integer pageNumber) {

        return itemRepository.searchItems(
                    categoryId, itemName, statusCondition, sortCondition, pageNumber);
    }
}
