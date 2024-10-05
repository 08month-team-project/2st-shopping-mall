package com.example.shoppingmall.domain.item.type;

import com.fasterxml.jackson.annotation.JsonFormat;


@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ClothingSize {
    XS, S, M, L, XL, XXL, ETC

}
