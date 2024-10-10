package com.example.shoppingmall.domain.order.type;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OrderResult {
    SUCCESS,
    FAIL_SOLD_OUT,
    FAIL_SALE_DATE_EXPIRATION,
    STOCK_SHORTAGE,
    NOT_FOUND_ITEM,
    INTERNAL_ERROR // 락 획득 시도 실패
}
