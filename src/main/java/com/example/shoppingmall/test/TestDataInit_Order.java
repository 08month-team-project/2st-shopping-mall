package com.example.shoppingmall.test;

import com.example.shoppingmall.domain.item.dao.CategoryRepository;
import com.example.shoppingmall.domain.item.dao.ClothingSizeRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.type.CategoryName;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.shoppingmall.domain.item.type.ItemStatus.IN_STOCK;

@Component
@RequiredArgsConstructor
public class TestDataInit_Order {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ClothingSizeRepository clothingSizeRepository;


    @Transactional
    @EventListener(value = ApplicationReadyEvent.class)
    public void initData() {

        // 유저 ===================================================
        Address address = Address.builder()
                .city("서울 특별시 강남구")
                .zipcode("1101").build();

        User user = userRepository.save(User.builder()
                .email("exampl1@gmail.com")
                .name("db")
                .nickname("db")
                .password("mypass1234")
                .gender(Gender.MALE)
                .phoneNumber("010-1234-1234")
                .address(address).build());

        // 카테고리 세팅 ============================================
        List<CategoryName> categoryNames = List.of(CategoryName.values());
        List<Category> categories = new ArrayList<>();

        for (CategoryName categoryName : categoryNames) {
            categories.add(new Category(categoryName));
        }
        // id 값까지 세팅된 카테고리 엔티티들로 다시 저장
        categories = categoryRepository.saveAll(categories);


        // Clothing Size 세팅 =======================================
        List<ClothingSize> clothingSizes = new ArrayList<>();

        ClothingSizeName[] values = ClothingSizeName.values();
        for (ClothingSizeName value : values) {
            clothingSizes.add(new ClothingSize(value));
        }
        clothingSizes = clothingSizeRepository.saveAll(clothingSizes);


        // 아이템 세팅 ==============================================
        List<Item> items = new ArrayList<>();

            String name = clothingSizes.get(0).getSizeName().name();

                Item item = itemSetting(user, "썸네일url",
                        "아이템이름",
                        categories.get(0),
                        0, 0,
                        IN_STOCK,
                        expiryDateTime(10, true));
                item.addItemStock(clothingSizes.get(0), 100);
                items.add(item);

            items = itemRepository.saveAll(items);
    }

    private Item itemSetting(User user,
                             String thumbnailUrl,
                             String itemName,
                             Category category,
                             int price,
                             long settingHits,
                             ItemStatus status,
                             LocalDateTime settingExpiryDateTime) {

        Item item = Item.builder()
                .description("설명")
                .user(user)
                .name(itemName)
                .price(price)
                .thumbnailUrl(thumbnailUrl)
                .hitCount(settingHits)
                .status(status)
                .expiredAt(settingExpiryDateTime)
                .build();
        item.addCategory(category);

        return item;
    }

    private LocalDateTime expiryDateTime(int days, boolean isPlus) {

        Duration duration = Duration.ofDays(days);
        return isPlus ? LocalDateTime.now().plus(duration) : LocalDateTime.now().minus(duration);
    }
}


