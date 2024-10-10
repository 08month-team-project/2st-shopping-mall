package com.example.shoppingmall.test;

import com.example.shoppingmall.domain.item.dao.CategoryRepository;
import com.example.shoppingmall.domain.item.dao.ClothingSizeRepository;
import com.example.shoppingmall.domain.item.dao.ItemRepository;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.domain.Item;
import com.example.shoppingmall.domain.item.type.CategoryName;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import com.example.shoppingmall.domain.user.dao.UserRepository;
import com.example.shoppingmall.domain.user.domain.User;
import com.example.shoppingmall.domain.user.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
//@Component
public class DataInit_CateAndSize implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DataInit_CateAndSize.class);
    private final CategoryRepository categoryRepository;
    private final ClothingSizeRepository clothingSizeRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // 카테고리 초기화
        for (CategoryName name : CategoryName.values()) {
            categoryRepository.save(new Category(name));
            logger.info("Saved category: {}", name);
        }

        // 사이즈 초기화
        for (ClothingSizeName sizeName : ClothingSizeName.values()) {
            clothingSizeRepository.save(new ClothingSize(sizeName));
            logger.info("Saved clothing size: {}", sizeName);
        }

        // 더미 사용자 생성
        User seller = User.builder()
                .name("sellerUsername")
                .nickname("sellerNickname")
                .password("password")
                .phoneNumber("010-2222-3333")
                .email("seller@example.com")
                .role(UserRole.SELLER)
                .build();

        userRepository.save(seller);

        // 이미지 리스트
        List<String> imageFiles = List.of(
                "/img1.jpg", "/img2.jpg", "/img3.png"
        );

        // 아이템 초기화 (총 3개)
        for (int i = 0; i < 3; i++) {
            Item item = Item.builder()
                    .name("상품명 예시 - " + (i + 1))
                    .price(29200)
                    .description("이 제품은 " + (i + 1) + "의 설명입니다.")
                    .user(seller)
                    .expiredAt(LocalDateTime.now().plusDays(30)) // 30일 후로 설정
                    .thumbnailUrl(imageFiles.get(i)) // 썸네일 이미지 추가
                    .build();

            // 사이즈 설정
            ClothingSize size = clothingSizeRepository.findBySizeName(ClothingSizeName.M).orElse(null);
            item.addItemStock(size, 10); // 사이즈와 수량 설정

            // 아이템 저장
            itemRepository.save(item);
            logger.info("Saved item: {}", item.getName());
        }
    }
}
