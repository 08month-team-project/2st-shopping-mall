package com.example.shoppingmall.domain.item.api;

import com.example.shoppingmall.domain.item.application.ItemService;
import com.example.shoppingmall.domain.item.dto.ItemDetailResponse;
import com.example.shoppingmall.domain.item.dto.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<Page<ItemResponse>> getItemMainPage(
            @RequestParam(name = "page", defaultValue = "1") int pageNum) {

        return ResponseEntity.ok(itemService.getItemMainPage(pageNum));
    }

    @GetMapping("/{item_id}")
    public ResponseEntity<ItemDetailResponse> getItemDetail(
            @PathVariable("item_id") long itemId) {

        return ResponseEntity.ok(itemService.getItemDetail(itemId));
    }
}
