package com.example.shoppingmall.domain.order.excepction;

import com.example.shoppingmall.domain.order.dto.OrderItemResponse;
import com.example.shoppingmall.global.exception.ErrorCode;
import com.example.shoppingmall.global.exception.ErrorResult;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderErrorResult extends ErrorResult {

    private OrderItemResponse orderItemResponse;

    public OrderErrorResult(ErrorCode errorCode, OrderItemResponse orderItemResponse) {
        super(errorCode);
        this.orderItemResponse = orderItemResponse;
    }
}
