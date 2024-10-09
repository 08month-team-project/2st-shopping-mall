package com.example.shoppingmall.test;

import com.example.shoppingmall.domain.item.dao.CategoryRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.type.CategoryName;
import com.example.shoppingmall.domain.item.type.ItemStatus;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.shoppingmall.domain.item.type.ItemStatus.IN_STOCK;
import static com.example.shoppingmall.domain.item.type.ItemStatus.ALL_OUT_OF_STOCK;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestDataInit_SearchItems {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    /*
    테스트코드으로 해결해보려했는데, 경우의 수가 너무 많은점, 눈으로 확인하기가 힘들고,
    테스트 코드에 서툰 탓에 일정이 계속 밀릴 것 같아서 직접 테스트 하는 방식으로 진행했습니다..
    평소에는 리스너를 주석처리해놓고, 혹시 테스트 하고 싶으실 땐 주석풀고 해보시면 됩니다.
    * */

    @Transactional
    //@EventListener(value = ApplicationReadyEvent.class)
    public void initData() {

        // 유저 ===================================================
        Address address = Address.builder()
                .city("서울 특별시 강남구 강남대로 123")
                .zipcode("1101").build();

        User user = userRepository.save(User.builder()
                .email("example@gmail.com")
                .name("홍길동")
                .nickname("길동이")
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


        // 아이템 세팅 ==============================================
        List<Item> items = new ArrayList<>();


        // 재고, 조회수 는 반비례 하게 하자( 재고, 조회수를 같게하니까 결과가 똑같아서 제대로 조건이 먹힌건지 파악하기 힘들었었음)
        for (Category category : categories) {
            String name = category.getCategoryName().name();

            int itemPrice = 1000;
            int itemHits = 1000;

            // 재고있음, 날짜유효, 가격 up, 조회수 down
            for (int i = 0; i < 10; i++) {
                items.add(itemSetting(user, name + i,
                        name + i,
                        category,
                        itemPrice++, itemHits--,
                        IN_STOCK,
                        expiryDateTime(i, true)));
            }

            itemPrice = 1000;
            itemHits = 1000;

            // 재고있음, 날짜만료, 가격 down, 조회수 up
            for (int i = 0; i < 10; i++) {
                items.add(itemSetting(user, name + i,
                        name + i,
                        category,
                        itemPrice--, itemHits++,
                        IN_STOCK,
                        expiryDateTime(i, false)));
            }

            itemPrice = 1000;
            itemHits = 1000;

            // 재고없음, 날짜유효 , 가격 down, 조회수 up
            for (int i = 0; i < 10; i++) {
                items.add(itemSetting(user, name + i,
                        name + i,
                        category,
                        itemPrice--, itemHits++,
                        ALL_OUT_OF_STOCK,
                        expiryDateTime(i, true)));
            }

            // 재고없음, 날짜만료 , 가격 up, 조회수 down
            for (int i = 0; i < 10; i++) {
                items.add(itemSetting(user, name + i,
                        name + i,
                        category,
                        itemPrice++, itemHits--,
                        ALL_OUT_OF_STOCK,
                        expiryDateTime(i, false)));
            }

        }

        // id, createdAt 이 세팅 된 엔티티로 다시 저장
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

        // 어제는 무슨 짓을 해도 안되더니만..
//        System.out.println("db에 들어갈때 만료일자 제대로 안됨 : " + item.getExpiredAt());

        return item;
    }

    private LocalDateTime expiryDateTime(int days, boolean isPlus) {

        Duration duration = Duration.ofDays(days);
        return isPlus ? LocalDateTime.now().plus(duration) : LocalDateTime.now().minus(duration);
    }
}
