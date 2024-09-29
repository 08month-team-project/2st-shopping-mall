package com.example.shoppingmall.domain.user.excepction;

import com.example.shoppingmall.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ErrorCode errorCode;
    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
