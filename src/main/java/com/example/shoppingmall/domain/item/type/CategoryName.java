package com.example.shoppingmall.domain.item.type;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum CategoryName {

    MALE("남성"),
    FEMALE("여성"),
    UNISEX("남녀공용"),
    CHILDREN("아동")
    ;

    private final String name;


    CategoryName(String name){
        this.name = name;
    }

}