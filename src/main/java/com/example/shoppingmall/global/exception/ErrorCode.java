package com.example.shoppingmall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    temporarily(null, null),
    USER_NOT_FOUND(NOT_FOUND,"사용자를 찾을 수 없습니다."),
    ALREADY_EXIST_USER(CONFLICT,"이미 존재하는 사용자입니다."),
    CREATE_USER_FAILED(INTERNAL_SERVER_ERROR,"회원가입에 실패했습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}

