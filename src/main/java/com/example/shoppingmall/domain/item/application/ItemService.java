package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.CategoryRepository;
import com.example.shoppingmall.domain.item.dao.ClothingSizeRepository;
import com.example.shoppingmall.domain.item.dao.ImageRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.dto.*;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.item.type.SortCondition;
import com.example.shoppingmall.domain.item.type.StatusCondition;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.shoppingmall.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Transactional
@Service
public class ItemService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ClothingSizeRepository clothSizeRepository;


    public ItemDetailResponse getItemDetail(long itemId) {

        Item item = itemRepository.findItemAndStockAndSeller(itemId)
                .orElseThrow(() -> new ItemException(NOT_FOUND_ITEM));

        return new ItemDetailResponse(item);
    }

    public ItemDetailImages getItemImages(long itemId) {
        return ItemDetailImages.of(itemId, imageRepository.findAllByItemId(itemId));
    }



    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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
    public SellerResponse itemRegister(RegisterRequest request, CustomUserDetails userDetails) {

        /*
         * CustomUserDetails에서 차라지 User 자체를 받아오면 더 편하지 않을까?
         * User user = userDetails.getUser();
         * 이미 시큐리티에서  "items/seller/register").hasAuthority("SELLER") 이주소를 통과 하였는데
         * 유저를 또 검증하고 디비를 다녀올 필요가 없는거 같음
         */
        User user = userRepository.findById(userDetails.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        // 리스트로 받아온 이미지들을 분류 하는 작업 임시로 첫번째로 들어온 사진을 썸네일 컬럼에 저장
        List<String> imgUrlList = request.getImagesUrl();
        String thumbnailUrl = imgUrlList.get(0);

        // Item 객체 생성
        Item item = Item.createItem(request, user, thumbnailUrl);
        item.addItemImage(imgUrlList);

        // 아이템 저장
        itemRepository.save(item);

        // 카테고리 조회 -> 캐싱으로 불필요한 디비 조회를 줄이면 더 좋겟지?
        Category category = categoryRepository.findByCategoryName(request.getCategory())
                .orElseThrow(() -> new ItemException(NOT_FOUND_CATEGORY));

        // 카테고리에 아이템 추가
        category.addItem(item); // Category에 연관 관계 설정

        // 사이즈 조회 > 이것도 마찬가지 이것들을 값이 자주 바뀔 이유가 없으니
        ClothingSize size = clothSizeRepository.findBySizeName(request.getSizeName())
                .orElseThrow(() -> new ItemException(NOT_FOUND_SIZE));

        // 재고 저장
        item.addItemStock(size,request.getStuck());

        // 성공 메시지 응답
        return SellerResponse.builder()
                .message("상품이 성공적으로 등록되었습니다.")
                .build();
    }

}
