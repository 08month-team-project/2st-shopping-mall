package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.*;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.domain.ItemHit;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private final ItemHitRepository itemHitRepository;

    private final ItemHitRedisService itemHitRedisService;

    // TODO
    //  getItemDetail 자체에 트랜잭션을 적용하던지, 아니면, 내부 메서드들에 requires new 속성의 트랜잭션을 적용해야할 듯 함
    //  혹은 조회를 아예의 별도의 서비스로 뺀 뒤 여기서 호출하게끔 해야할지도
    public ItemDetailResponse getItemDetail(long itemId,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {

        Item item = itemRepository.findItemAndStockAndSeller(itemId)
                .orElseThrow(() -> new ItemException(NOT_FOUND_ITEM));

        hitItem(item, request, response);

        // TODO 조회수가 변경 될걸로 반영해야함
        return new ItemDetailResponse(item);
    }

    // 트랜잭션을 안에 적용하려면 public 이어야함
    public void hitItem(Item item, HttpServletRequest request, HttpServletResponse response) {

        // 로그인 유저일 경우
        if (request.getUserPrincipal() != null &&
                request.getUserPrincipal() instanceof CustomUserDetails userDetails) {

            handleLoginUserItemHit(item, userDetails);
        }

        handleGuestItemHit(item, request, response);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLoginUserItemHit(Item item, CustomUserDetails userDetails) {
        // 조회한 기록이 없음
        if (!itemHitRepository.existsByUserIdAndItemId(
                userDetails.getUserId(), item.getId())) {

            // TODO 동시성 문제
            item.increaseHit();  // 이게 지금 변경 감지가 안되나..?
            itemHitRepository.save(new ItemHit(item.getId(), userDetails.getUserId()));

            itemRepository.flush();
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleGuestItemHit(Item item, HttpServletRequest request, HttpServletResponse response) {
        // 비로그인 조회일 경우 쿠키를 꺼내봐야한다.
        Cookie viewCookie = getHitCookie(request);

        // 해당 물품을 조회한 적이 없다는 상황
        // 그러나 쿠키 값을 삭제하거나 조작했을 수 있는 상황
        if (viewCookie == null || !viewCookie.getValue().contains("/" + item.getId() + "/")) { // 앞뒤로 붙여야함 안그럼 /1 , /11


            // 진짜 보지 않았는지 레디스에서 찾아본다.
            // true 면 조회수 증가된 것임
            if (itemHitRedisService.handleItemHit(item.getId(), request)) {

                // TODO 여기서 동시성 문제가 발생 (조회수 ++ 를 것을 레디스에서만 한다면 상관이 없는데, 디비에서 직접 ++ 하는 부분이 문제)
                // 문제는 트랜잭션 내에서 돌아가야하는데..?
                item.increaseHit();
            }

            createHitCookie(item, response, viewCookie);
        }
    }

    private void createHitCookie(Item item, HttpServletResponse response, Cookie hitCookie) {

        // 조회수 관련 쿠키 자체는 있는데, 내부에 해당 item 이 없을 때
        if (hitCookie != null &&
                (!hitCookie.getValue().contains("/" + item.getId() + "/"))) {

            hitCookie.setValue(hitCookie.getValue() + "/" + item.getId() + "/");
            hitCookie.setPath("/");
            hitCookie.setMaxAge(60 * 60 * 24);
            return;
        }

        // 조회수 관련 쿠키 자체가 없음
        Cookie newCookie = new Cookie("itemHit", "/" + item.getId() + "/");
        newCookie.setPath("/");
        newCookie.setMaxAge(60 * 60 * 24);
        response.addCookie(newCookie);

    }

    // 쿠키 추출
    private Cookie getHitCookie(HttpServletRequest request) {
        Cookie viewCookie = null;

        for (Cookie cookie : request.getCookies()) {
            if ("itemHit".equals(cookie.getName())) {
                viewCookie = cookie;
            }
        }
        return viewCookie;
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
        item.addItemStock(size, request.getStuck());

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
