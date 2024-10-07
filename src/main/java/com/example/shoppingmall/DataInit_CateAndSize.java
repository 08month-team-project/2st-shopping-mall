package com.example.shoppingmall;

import com.example.shoppingmall.domain.item.dao.CategoryRepository;
import com.example.shoppingmall.domain.item.dao.ClothingSizeRepository;
import com.example.shoppingmall.domain.item.domain.Category;
import com.example.shoppingmall.domain.item.domain.ClothingSize;
import com.example.shoppingmall.domain.item.type.CategoryName;
import com.example.shoppingmall.domain.item.type.ClothingSizeName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInit_CateAndSize implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ClothingSizeRepository clothingSizeRepository;

    @Override
    public void run(String... args) throws Exception {
        for (CategoryName name : CategoryName.values()) {
            categoryRepository.save(new Category(name));
        }

        for (ClothingSizeName sizeName : ClothingSizeName.values()) {
            clothingSizeRepository.save(new ClothingSize(sizeName));
        }
    }
}
