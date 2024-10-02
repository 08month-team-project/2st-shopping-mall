package com.example.shoppingmall.domain.item.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ItemStatus {

    IN_STOCK("재고있음"),
    OUT_OF_STOCK("재고없음");

    private final String name;

    ItemStatus(String name) {
        this.name = name;
    }

}
