package com.example.shoppingmall.domain.item.api;

import com.example.shoppingmall.domain.item.application.ItemService;
import com.example.shoppingmall.domain.item.dto.ItemDetailResponse;
import com.example.shoppingmall.domain.item.dto.ItemResponse;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/{item_id}")
    public ResponseEntity<ItemDetailResponse> getItemDetail(
            @PathVariable("item_id") long itemId) {

        return ResponseEntity.ok(itemService.getItemDetail(itemId));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemResponse>> searchItems(
            @RequestParam(name = "category_id", required = false) Long categoryId,
            @RequestParam(name = "item_name", required = false) String itemName,
            @RequestParam(name = "status_condition", required = false) StatusCondition statusCondition,
            @RequestParam(name = "sort_condition", required = false) SortCondition sortCondition,
            @RequestParam(name = "page_number", required = false) Integer pageNumber
    ) {

        return ResponseEntity.ok(itemService.searchItems(
                categoryId, itemName, statusCondition, sortCondition, pageNumber));
    }
}
