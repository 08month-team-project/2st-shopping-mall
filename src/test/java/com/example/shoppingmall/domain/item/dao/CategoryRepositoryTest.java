package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.example.shoppingmall.domain.item.type.CategoryName.*;
import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리를 저장한 후에 조회하는 테스트")
    void findAllCategoryName() {
        // given
        // 실행 될때마다 모든 카테고리가 등록 되어 있음
        // when
        List<Category> categories = categoryRepository.findAll();

        // then
        assertThat(categories).hasSize(4)
                .extracting("categoryName")
                .containsExactlyInAnyOrder(MALE,UNISEX ,FEMALE, CHILDREN);
    }
}