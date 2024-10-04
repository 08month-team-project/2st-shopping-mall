package com.example.shoppingmall.domain.item.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;


/**
 * 검색 요청값 용도 (참고: ItemStatus 와는 다른용도)
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum StatusCondition {

    AVAILABLE("구매가능"),  // 구매자 검색 전용

    //EXPIRATION("일자 만료") // 판매자 검색 전용, 실제 기능계획에는 없는 부분이지만 예시로 넣어보았음
    ;

    private final String name;

    StatusCondition(String name) {
        this.name = name;
    }

}
