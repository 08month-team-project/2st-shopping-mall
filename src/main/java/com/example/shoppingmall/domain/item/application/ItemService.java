package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.*;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.dto.*;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.domain.user.type.UserRole;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.shoppingmall.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ClothingSizeRepository clothSizeRepository;
    private final ItemStockRepository itemStockRepository;


    public ItemDetailResponse getItemDetail(long itemId) {

        Item item = itemRepository.findItemAndStockAndSeller(itemId)
                .orElseThrow(() -> new ItemException(NOT_FOUND_ITEM));

        return new ItemDetailResponse(item);
    }

    public ItemDetailImages getItemImages(long itemId) {
        return ItemDetailImages.of(itemId, imageRepository.findAllByItemId(itemId));
    }



    public Page<ItemResponse> searchItems(Long categoryId,
                                          String itemName,
                                          StatusCondition statusCondition,
                                          SortCondition sortCondition,
                                          Integer pageNumber) {

        return itemRepository.searchItems(
                categoryId, itemName, statusCondition, sortCondition, pageNumber);
    }

    // 카테고리 목록 전체 조회
    public CategoryResponse getCategoryList() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryList> categoryList = categories.stream()
                .map(CategoryList::fromEntity)
                .collect(Collectors.toList());

        return CategoryResponse.builder()
                .categoryList(categoryList)
                .build();
    }

    // 옷 상품 싸이즈 목록 전체 조회
    public SizeResponse getSizeList() {
        List<ClothingSize> sizes = clothSizeRepository.findAll();

        List<SizeItem> sizeItems = sizes.stream()
                .map(SizeItem::fromEntity)
                .collect(Collectors.toList());

        return SizeResponse.builder()
                .sizeItemList(sizeItems)
                .build();
    }

    // 아이템 등록 기능
    @Transactional
    public SellerResponse itemRegister(RegisterRequest request, CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        if (!UserRole.SELLER.equals(user.getRole())) {
            throw new UserException(NOT_ROLE_SELLER);
        }

        // 리스트로 받아온 이미지들을 분류 하는 작업 임시로 첫번째로 들어온 사진을 썸네일 컬럼에 저장
        List<String> imgUrlList = request.getImagesUrl();
        String thumbnailUrl = imgUrlList.get(0);

        // Item 객체 생성
        Item item = Item.createItem(request, user, thumbnailUrl);
        item.addItemImage(imgUrlList);

        // 아이템 저장
        itemRepository.save(item);

        Category category = categoryRepository.findByCategoryName(request.getCategory())
                .orElseThrow(() -> new ItemException(NOT_FOUND_CATEGORY));

        // 카테고리에 아이템 추가
        category.addItem(item); // Category에 연관 관계 설정

        ClothingSize size = clothSizeRepository.findBySizeName(request.getSizeName())
                .orElseThrow(() -> new ItemException(NOT_FOUND_SIZE));

        // 재고 저장
        item.addItemStock(size,request.getStuck());

        // 성공 메시지 응답
        return SellerResponse.builder()
                .message("상품이 성공적으로 등록되었습니다.")
                .build();
    }

    public Page<SellerItemResponse> searchPageComplex(CustomUserDetails userDetails, ItemStatus status, Pageable pageable, Integer page) {
        // 페이지 번호를 조정
        Pageable pages = PageRequest.of(page - 1, pageable.getPageSize());

        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        return itemRepository.findAllByUserAndStatus(user.getId(), status, pages);
    }

    @Transactional
    public SellerResponse updateItemStock(Long id, UpdateItemRequest request) {
        // 아이템 조회
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemException(NOT_FOUND_ITEM));

        ClothingSize size = clothSizeRepository.findBySizeName(request.getSizeName())
                .orElseThrow(() -> new ItemException(NOT_FOUND_SIZE));

        // 재고 및 상태 업데이트
        item.updateStockAndStatus(size, request.getStuck());

        return SellerResponse.builder()
                .message("수정 완료 되었습니다.")
                .build();
    }
}
