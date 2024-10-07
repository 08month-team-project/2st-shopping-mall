package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.CategoryRepository;
import com.example.shoppingmall.domain.item.dao.ClothingSizeRepository;
import com.example.shoppingmall.domain.item.dao.ImageRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.domain.ItemImage;
import com.example.shoppingmall.domain.item.dto.RegisterRequest;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.item.type.CategoryName;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.excepction.UserException;
import com.example.shoppingmall.global.security.detail.CustomUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.shoppingmall.domain.item.type.CategoryName.MALE;
import static com.example.shoppingmall.domain.item.type.ClothingSizeName.M;
import static com.example.shoppingmall.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceRegisterTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ClothingSizeRepository clothingSizeRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ItemService itemService;

    @DisplayName("상품 등록 성공 테스트")
    @Test
    void itemRegister_success() {
        // given
        LocalDateTime expirationDateTime = LocalDateTime.now().plusDays(30);
        RegisterRequest registerRequest = createRegisterRequest("테스트 상품", 2000, MALE, M, 200, "테스트 예시", expirationDateTime);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
        when(categoryRepository.findByCategoryName(MALE)).thenReturn(Optional.of(mock(Category.class)));
        when(clothingSizeRepository.findBySizeName(M)).thenReturn(Optional.of(mock(ClothingSize.class)));

        // when
        itemService.itemRegister(registerRequest, mockCustomUserDetails());

        // then
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(categoryRepository, times(1)).findByCategoryName(MALE);
        verify(clothingSizeRepository, times(1)).findBySizeName(M);

        // 아이템 저장 시 썸네일과 이미지가 올바르게 저장되었는지 검증
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(itemCaptor.capture());
        Item savedItem = itemCaptor.getValue();

        // 썸네일 이미지 URL 검증
        assertThat(savedItem.getThumbnailUrl()).isEqualTo("http://example.com/image1.jpg"); // 아이템의 썸네일

        // 저장된 ItemImage URL 검증
        List<ItemImage> savedImages = savedItem.getImages();
        assertThat(savedImages).hasSize(2); // 이미지 개수 확인
        assertThat(savedImages.get(0).getImageUrl()).isEqualTo("http://example.com/image2.jpg"); // 첫 번째 이미지
        assertThat(savedImages.get(1).getImageUrl()).isEqualTo("http://example.com/image3.jpg"); // 두 번째 이미지
    }

    @DisplayName("상품 등록 실패 테스트 - 사용자 찾지 못함")
    @Test
    void itemRegister_fail_userNotFound() {
        // given
        RegisterRequest registerRequest = createRegisterRequest("테스트 상품", 2000, MALE, M, 200, "테스트 예시", LocalDateTime.now().plusDays(30));

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));

        // when & then
        assertThatThrownBy(() -> itemService.itemRegister(registerRequest, mockCustomUserDetails()))
                .isInstanceOf(UserException.class)
                .hasMessage(USER_NOT_FOUND.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @DisplayName("상품 등록 실패 테스트 - 카테고리 찾지 못함")
    @Test
    void itemRegister_fail_categoryNotFound() {
        // given
        RegisterRequest registerRequest = createRegisterRequest("테스트 상품", 2000, MALE, M, 200, "테스트 예시", LocalDateTime.now().plusDays(30));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
        when(categoryRepository.findByCategoryName(MALE)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> itemService.itemRegister(registerRequest, mockCustomUserDetails()))
                .isInstanceOf(ItemException.class)
                .hasMessage(NOT_FOUND_CATEGORY.getMessage());
        verify(categoryRepository, times(1)).findByCategoryName(MALE);
    }

    @DisplayName("상품 등록 실패 테스트 - 사이즈 찾지 못함")
    @Test
    void itemRegister_fail_sizeNotFound() {
        // given
        RegisterRequest registerRequest = createRegisterRequest("테스트 상품", 2000, MALE, M, 200, "테스트 예시", LocalDateTime.now().plusDays(30));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
        when(categoryRepository.findByCategoryName(MALE)).thenReturn(Optional.of(mock(Category.class)));
        when(clothingSizeRepository.findBySizeName(M)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> itemService.itemRegister(registerRequest, mockCustomUserDetails()))
                .isInstanceOf(ItemException.class)
                .hasMessage(NOT_FOUND_SIZE.getMessage());
        verify(categoryRepository, times(1)).findByCategoryName(MALE);
        verify(clothingSizeRepository, times(1)).findBySizeName(M);
    }


    private CustomUserDetails mockCustomUserDetails() {
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getUserId()).thenReturn(1L);
        return customUserDetails;
    }

    private RegisterRequest createRegisterRequest(String name, Integer price, CategoryName categoryName, ClothingSizeName sizeName, Integer stuck, String description, LocalDateTime expirationDateTime) {
        List<String> images = List.of("http://example.com/image1.jpg", "http://example.com/image2.jpg", "http://example.com/image3.jpg");
        return RegisterRequest.builder()
                .imagesUrl(images)
                .name(name)
                .price(price)
                .category(categoryName)
                .sizeName(sizeName)
                .stuck(stuck)
                .description(description)
                .expiredAt(expirationDateTime)
                .build();
    }
}
