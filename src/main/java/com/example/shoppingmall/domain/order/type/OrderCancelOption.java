package com.example.shoppingmall.domain.order.type;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OrderCancelOption {
    CANCEL_ENTIRE_ORDER("전체 주문 취소"),
    CANCEL_OUT_OF_STOCK_ONLY("품절 상품만 취소");


    OrderCancelOption(String message) {
    }
}
