package com.example.shoppingmall.domain.item.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum SortCondition {

    PRICE("가격순"),
    HITS("조회순"),
    SALES("판매순"),
    DEADLINE("마감임박순"),
    LATEST("최신순");


    private final String name;

    SortCondition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
