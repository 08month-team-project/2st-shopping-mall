package com.example.shoppingmall.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CensorValidator implements ConstraintValidator<BadWordFilter, String> {

    @Autowired
    private CensorService censorService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null 값은 유효함
        }
        // 비속어가 포함되어 있는지 검사
        return !censorService.containsBadWords(value);
    }
}
