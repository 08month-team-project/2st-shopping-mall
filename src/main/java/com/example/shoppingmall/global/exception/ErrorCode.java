package com.example.shoppingmall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //ItemException
    NOT_FOUND_ITEM(NOT_FOUND, "물품을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}

