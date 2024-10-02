package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.domain.ItemStock;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.Address;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.shoppingmall.domain.item.type.ClothingSize.*;
import static org.assertj.core.api.Assertions.assertThat;


//@Rollback(value = false)
//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;
    private List<Item> items;


    @BeforeEach
    public void init() {
        items = new ArrayList<>();

        Address address = Address.builder()
                .street("강남대로 123")
                .city("서울 특별시 강남구")
                .zipcode("1101").build();

        user = User.builder()
                .email("example@gmail.com")
                .name("홍길동")
                .nickname("길동이")
                .password("mypass1234")
                .gender(Gender.MALE)
                .phoneNumber("010-1234-1234")
                .address(address).build();

        userRepository.save(user);


        Item item1 = Item.builder()
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

        items.add(item1);
        itemRepository.saveAll(items);
    }

    @DisplayName("물품정보와 해당물품에 속한 옵션들(사이즈&재고)과 판매자 정보를 한번에 가져옵니다.")
    @Test
    void findItemAndStockAndSeller() {
        Item savedItem = items.get(0);

        Item itemAndStockAndSeller = itemRepository.findItemAndStockAndSeller(savedItem.getId()).get();

        assertThat(itemAndStockAndSeller).isNotNull();

        assertThat(itemAndStockAndSeller.getId()).isEqualTo(savedItem.getId());
        assertThat(itemAndStockAndSeller.getName()).isEqualTo(savedItem.getName());
        assertThat(itemAndStockAndSeller.getPrice()).isEqualTo(savedItem.getPrice());

        assertThat(itemAndStockAndSeller.getThumbnailUrl()).isEqualTo(savedItem.getThumbnailUrl());
        assertThat(itemAndStockAndSeller.getExpiredAt()).isEqualTo(savedItem.getExpiredAt());
        assertThat(itemAndStockAndSeller.getDescription()).isEqualTo(savedItem.getDescription());

        List<ItemStock> stocks = itemAndStockAndSeller.getStocks();

        assertThat(stocks.size()).isEqualTo(savedItem.getStocks().size());
    }
}