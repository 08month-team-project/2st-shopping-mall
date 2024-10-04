package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.CategoryRepository;
import com.example.shoppingmall.domain.item.dao.ClothSizeRepository;
import com.example.shoppingmall.domain.item.dao.ImageRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.ClothSize;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.dto.*;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.shoppingmall.global.exception.ErrorCode.NOT_FOUND_ITEM;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ClothSizeRepository clothSizeRepository;


    public ItemDetailResponse getItemDetail(long itemId) {

        Item item = itemRepository.findItemAndStockAndSeller(itemId)
                .orElseThrow(() -> new ItemException(NOT_FOUND_ITEM));

        return new ItemDetailResponse(item, imageRepository.findAllByItemId(item.getId()));
    }

    // 카테고리 목록 전체 조회
    public CategoryResponse getCategoryList() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryItem> categoryItems = categories.stream()
                .map(CategoryItem::fromEntity)
                .collect(Collectors.toList());

        return CategoryResponse.builder()
                .categoryList(categoryItems)
                .build();
    }

    // 옷 상품 싸이즈 목록 전체 조회
    public SizeResponse getSizeList() {
        List<ClothSize> sizes = clothSizeRepository.findAll();

        List<SizeItem> sizeItems = sizes.stream()
                .map(SizeItem::fromEntity)
                .collect(Collectors.toList());

        return SizeResponse.builder()
                .sizeItemList(sizeItems)
                .build();
    }
}
