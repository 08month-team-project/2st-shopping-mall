package com.example.shoppingmall.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CensorValidator.class) // 커스텀 Validator 클래스
@Target({ElementType.FIELD}) // 필드에만 적용 가능
@Retention(RetentionPolicy.RUNTIME) // 런타임 동안 유지
public @interface BadWordFilter {
    String message() default "비속어가 포함되어 있습니다."; // 기본 메시지

    Class<?>[] groups() default {}; // 그룹 정보

    Class<? extends Payload>[] payload() default {}; // 페이로드 정보
}
