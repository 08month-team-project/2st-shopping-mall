package com.example.shoppingmall.domain.item.dao;

import com.example.shoppingmall.domain.item.domain.ClothSize;
import com.example.shoppingmall.domain.item.type.ClothingSize;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static com.example.shoppingmall.domain.item.type.ClothingSize.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClothSizeRepositoryTest {

    @Autowired
    private ClothSizeRepository clothSizeRepository;

    @Test
    @DisplayName("옷 싸이즈를 저장한 후에 조회하는 테스트")
    void findAllClothSize() {
        // given
        ClothSize clothSizeXS = createClothSize(XS);
        ClothSize clothSizeS = createClothSize(S);
        ClothSize clothSizeM = createClothSize(M);
        ClothSize clothSizeL = createClothSize(L);
        ClothSize clothSizeXL = createClothSize(XL);
        ClothSize clothSizeETC = createClothSize(ETC);
        clothSizeRepository.saveAll(List.of(clothSizeXS,clothSizeS,clothSizeM,clothSizeL,clothSizeXL,clothSizeETC));
        // when
        List<ClothSize> clothSizeList = clothSizeRepository.findAll();
        // then
        assertThat(clothSizeList).hasSize(6)
                .extracting("sizeName")
                .containsExactly(XS,S,M,L,XL,ETC);
    }

    private ClothSize createClothSize(ClothingSize clothingSize) {
        return ClothSize.of(clothingSize);
    }

}