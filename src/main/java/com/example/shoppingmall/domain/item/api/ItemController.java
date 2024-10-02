package com.example.shoppingmall.domain.item.api;

import com.example.shoppingmall.domain.item.application.ItemService;
import com.example.shoppingmall.domain.item.application.S3Service;
import com.example.shoppingmall.domain.item.dto.ItemDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final S3Service s3Service;

    @GetMapping("/{item_id}")
    public ResponseEntity<ItemDetailResponse> getItemDetail(
            @PathVariable("item_id") long itemId) {

        return ResponseEntity.ok(itemService.getItemDetail(itemId));
    }

    // 이미지 업로드
    @PostMapping("/images/upload")
    public ResponseEntity<List<String>> getResignedUrls(@RequestParam("images") List<MultipartFile> multipartFiles) {
        List<String> urls = s3Service.createUrlsForUpload(multipartFiles);
        return ResponseEntity.ok(urls);
    }

}
