package com.example.shoppingmall.domain.item.api;

import com.example.shoppingmall.domain.item.application.ItemService;
import com.example.shoppingmall.domain.item.application.S3Service;
import com.example.shoppingmall.domain.item.dto.*;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000"},
        allowCredentials = "true", maxAge = 3600,
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.OPTIONS},
        exposedHeaders = {"Authorization", "Content-Type"})
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final S3Service s3Service;

    /**
     * 참고: 세션 방식은 쓰지 않기로 하였음
     * <p>
     * 1. userDetails 가 null 이 아니다 -> db 에 조회한 기록이 있는지 확인한다.
     * -> 없으면 데이터 생성
     * -> 조회수 증가
     * <p>
     * (비회원 조회: userDetails 가 null)
     * 2. 쿠키를 확인
     * -> 쿠키에 조회내용이 있다 (이미 조회) -> 검증할 필요없이 그냥 조회수 증가를 안하면 된다.
     * -> 쿠키에 조회내용이 없다.
     * -> 정말 안봤다. or  삭제 등의 조작의 가능성이 있다.
     * <p>
     * 3. 이때 ip를 확인한다. (특정 시간내에 같은 ip에서 조회한 적이 있는지 확인) - 이걸 저장할 시간, db 등은 고민중
     * -> 없다면 쿠키생성 , 조회수 증가
     * (하지만? 같은 장소에서 여러사람이 같은 것을 조회한다면..? -> 보지 않았음에도 조회수가 중복처리 될 것이다.)
     * (특정 시간내에 1회가 아닌 n회이상 조회했다면 중복방지를 하겠다는 설정을 해도 되겠지만,
     * 만약에 같은 장소에서 50명이 같은 것을 봤는데, 설정한 값이 20이면? 30명분의 조회수는 중복처리된다.
     * 또 다른 유저를 식별할 수 있는 값이 없을까? -> [User-Agent]: 사용자의 브라우저와 OS 등에 대한 정보를 제공
     * <p>
     * 4. ip 뿐만 아니라 User-Agent 값도 같이 확인하는 것으로 한다.
     * (물론 같은 장소에서 서로 다른 사람이 같은 브라우저, os 등을 사용할 가능성도 있을 수 있다고 생각한다.)
     * <p>
     * 결국 동일 ip, User-Agent 의 경우 n시간 내에 n번 조회가 발생할 경우 중복 처리하는 수 밖에 없지않을까싶다... 흠... 애매해다..
     * <p>
     * <p>
     * <p>
     * - 로그인회원이 조회한 정보(영구저장)
     * - 비회원 조회 정보 (일정 시간만 저장)  -> 조회 관련이라 접근이 빠른 레디스를 써볼 까 하는데,
     * <p>
     * 일단 조회수 증가 자체를 매번 rdbms에 있는 데이터를 꺼내서 증가시키는게 성능상 문제가 있을 수도 있을 것 같은데,
     * 레디스에 처리하다가 나중에 한꺼번에 rdbms에 적용시킬 수도 있겠지만..? 흠..
     * <p>
     * <p>
     * key 값: ip + agent 으로 하면 어떨까? ->
     * 너무 긴가..? 한두개 저장할 것도 아니고 key값 자체가 길면 뭔가 메모리를 많이 먹지 않을려나? -> 이걸 고유한 짧은 값으로 만들 수 없나? -> 해시화?
     */

    @GetMapping("/{item_id}")
    public ResponseEntity<ItemDetailResponse> getItemDetail(
            @PathVariable("item_id") long itemId,
            HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(itemService
                .getItemDetail(itemId, request, response));
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

    // 상품등록
    @PostMapping("/seller/register")
    public ResponseEntity<SellerResponse> itemResister(@Valid @RequestBody RegisterRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        SellerResponse response = itemService.itemRegister(request, userDetails);
        return ResponseEntity.ok(response);
    }

    // 물품 리스트
    @GetMapping("/status")
    public ResponseEntity<Page<SellerItemResponse>> searchItems(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @RequestParam ItemStatus status,
                                                                @PageableDefault(size = 3) Pageable pageable,
                                                                @RequestParam(defaultValue = "1") Integer page) {
        Page<SellerItemResponse> result = itemService.searchPageComplex(userDetails,status, pageable,page);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{item-id}/stock")
    public ResponseEntity<SellerResponse> updateItemStock(@PathVariable(name = "item-id") Long id,
                                                          @RequestBody UpdateItemRequest request) {
        SellerResponse response = itemService.updateItemStock(id, request);
        return ResponseEntity.ok(response);
    }
}
