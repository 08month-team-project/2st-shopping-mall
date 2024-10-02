package com.example.shoppingmall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    temporarily(null, null),
    INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "허용되지 않는 파일 형식입니다. JPEG, JPG, PNG 파일만 가능합니다."),
    IMAGE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "파일 크기가 1MB를 초과합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

