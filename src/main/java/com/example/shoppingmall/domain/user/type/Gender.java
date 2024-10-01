package com.example.shoppingmall.domain.user.type;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Gender {

    MALE, FEMALE
}
