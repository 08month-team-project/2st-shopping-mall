package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.type.CategoryName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.example.shoppingmall.domain.item.type.CategoryName.*;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리를 저장한 후에 조회하는 테스트")
    void findAllCategoryName() {
        // given
        Category categoryMan = createCategoryName(MALE);
        Category categoryWoman = createCategoryName(FEMALE);
        Category categoryChild = createCategoryName(CHILDREN);
        categoryRepository.saveAll(List.of(categoryMan, categoryWoman, categoryChild));

        // when
        List<Category> categories = categoryRepository.findAll();

        // then
        assertThat(categories).hasSize(3)
                .extracting("name")
                .containsExactlyInAnyOrder(MALE, FEMALE, CHILDREN);
    }

    private Category createCategoryName(CategoryName type) {
        return Category.of(type);
    }
}