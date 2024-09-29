package com.example.shoppingmall.domain.item.excepction;

import com.example.shoppingmall.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ItemException extends RuntimeException {
    private final ErrorCode errorCode;
    public ItemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
