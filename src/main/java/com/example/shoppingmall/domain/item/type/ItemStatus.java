package com.example.shoppingmall.domain.item.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ItemStatus {

    IN_STOCK("재고있음"),
    ALL_OUT_OF_STOCK("전체품절"); // 물품에 속하는 모든 옵션(사이즈 등) 의 재고가 없을 때 체크

    private final String name;

    ItemStatus(String name) {
        this.name = name;
    }

}
