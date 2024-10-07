package com.example.shoppingmall.domain.item.api;

import com.example.shoppingmall.domain.item.application.ItemService;
import com.example.shoppingmall.domain.item.application.S3Service;
import com.example.shoppingmall.domain.item.dto.*;
import jakarta.validation.Valid;
import com.example.shoppingmall.domain.item.dto.ItemDetailImages;
import com.example.shoppingmall.domain.item.dto.ItemDetailResponse;
import com.example.shoppingmall.domain.item.dto.ItemResponse;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final S3Service s3Service;


    @GetMapping("/{item_id}")
    public ResponseEntity<ItemDetailResponse> getItemDetail(
            @PathVariable("item_id") long itemId) {

        return ResponseEntity.ok(itemService.getItemDetail(itemId));
    }

    @GetMapping("/{item_id}/images")
    public ResponseEntity<ItemDetailImages> getItemImages(
            @PathVariable("item_id") long itemId) {

        return ResponseEntity.ok(itemService.getItemImages(itemId));
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

    // 이미지 업로드
    @PostMapping("/images/upload")
    public ResponseEntity<List<String>> getResignedUrls(@RequestParam("images") List<MultipartFile> multipartFiles) {
        List<String> urls = s3Service.createUrlsForUpload(multipartFiles);
        return ResponseEntity.ok(urls);
    }

    // 카테고리 조회 -> 추후 캐싱 도전
    @GetMapping("/categories")
    public ResponseEntity<CategoryResponse> getCategoryList() {
        CategoryResponse response = itemService.getCategoryList();
        return ResponseEntity.ok(response);
    }

    // size 조회 -> 추후 캐싱 도전
    @GetMapping("/size")
    public ResponseEntity<SizeResponse> getSizeList() {
        SizeResponse response = itemService.getSizeList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/seller/register")
    public ResponseEntity<SellerResponse> itemResister(@Valid @RequestBody RegisterRequest request) {
        SellerResponse response = itemService.itemResister(request);
        return ResponseEntity.ok(response);
    }
}
