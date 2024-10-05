package com.example.shoppingmall.domain.item.application;

import com.example.shoppingmall.domain.item.dao.ImageRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.domain.ItemImage;
import com.example.shoppingmall.domain.item.dto.ItemDetailResponse;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.shoppingmall.domain.item.type.ClothingSize.*;
import static com.example.shoppingmall.global.exception.ErrorCode.NOT_FOUND_ITEM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ItemService itemService;


    private User user;
    private List<Item> items;
    private List<ItemImage> itemImages;


    @BeforeEach
    public void init() {

        user = User.builder()
                .id(1L)
                .email("example@gmail.com")
                .name("홍길동")
                .nickname("길동이")
                .password("mypass1234")
                .gender(Gender.MALE)
                .phoneNumber("010-1234-1234")
                .address(Address.builder()
                        .city("서울 특별시 강남구 강남대로 123")
                        .zipcode("1101").build())
                .build();

        Item item1 = Item.builder()
                .id(1L)
                .user(user)
                .name("황금바지")
                .price(10000000)
                .thumbnailUrl("황금바지 썸네일")
                .expiredAt(LocalDateTime.of(2024, 10, 20, 0, 0))
                .hitCount(0L)
                .description("당신도 입을 수 있어요!")
                .stocks(new ArrayList<>())
                .images(new ArrayList<>())
                .build();

        item1.addStockOption(S, 4);
        item1.addStockOption(L, 3);
        item1.addStockOption(XL, 2);

        itemImages = new ArrayList<>();
        itemImages.add(new ItemImage(1L, item1, "황금바지 이미지1"));
        itemImages.add(new ItemImage(2L, item1, "황금바지 이미지2"));
        itemImages.add(new ItemImage(3L, item1, "황금바지 이미지3"));

        for (ItemImage itemImage : itemImages) {
            item1.addImage(itemImage.getImageUrl());
        }

        items = new ArrayList<>();
        items.add(item1);
    }

    @Test
    void getItemDetail_Success() {

        // given
        Item item = items.get(0);

        // then
        when(itemRepository.findItemAndStockAndSeller(item.getId()))
                .thenReturn(Optional.of(item));

        when(imageRepository.findAllByItemId(item.getId()))
                .thenReturn(itemImages);

        // then
        ItemDetailResponse response = itemService.getItemDetail(item.getId());

        assertThat(response.getItemId()).isEqualTo(item.getId());
        assertThat(response.getItemName()).isEqualTo(item.getName());
        assertThat(response.getItemPrice()).isEqualTo(item.getPrice());
        assertThat(response.getDescription()).isEqualTo(item.getDescription());
        assertThat(response.getHits()).isEqualTo(item.getHitCount());

        assertThat(response.getCreatedAt()).isEqualTo(item.getCreatedAt());
        assertThat(response.getExpiredAt()).isEqualTo(item.getExpiredAt());

        assertThat(response.getSellerId()).isEqualTo(item.getUser().getId());
        assertThat(response.getSellerNickname()).isEqualTo(item.getUser().getNickname());

        assertThat(response.getStockList().size()).isEqualTo(item.getStocks().size());
        assertThat(response.getItemImageList().size()).isEqualTo(itemImages.size());
    }

    @Test
    void getItemDetail_Fail() {

        when(itemRepository.findItemAndStockAndSeller(any()))
                .thenThrow(new ItemException(NOT_FOUND_ITEM));

        assertThrows(ItemException.class, () -> itemService.getItemDetail(-1L));
    }
}