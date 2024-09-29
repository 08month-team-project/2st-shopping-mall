package com.example.shoppingmall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    temporarily(null, null),
    ;


    private final HttpStatus httpStatus;
    private final String message;
}

