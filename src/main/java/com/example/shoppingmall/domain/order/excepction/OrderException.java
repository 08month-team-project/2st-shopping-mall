package com.example.shoppingmall.domain.order.excepction;

import com.example.shoppingmall.domain.order.dto.OrderItemResponse;
import com.example.shoppingmall.global.exception.ErrorCode;
import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {
    private final ErrorCode errorCode;
    private final OrderItemResponse orderItemResponse;

    public OrderException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.orderItemResponse = null;
    }

    public OrderException(ErrorCode errorCode, OrderItemResponse orderItemResponse) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.orderItemResponse = orderItemResponse;
    }
}
