package com.example.shoppingmall.domain.cart.excepction;

import com.example.shoppingmall.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class CartException extends RuntimeException {

    private final ErrorCode errorCode;

    public CartException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
