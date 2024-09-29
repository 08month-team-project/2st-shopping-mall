package com.example.shoppingmall.domain.order.excepction;

import com.example.shoppingmall.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {
    private final ErrorCode errorCode;
    public OrderException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
