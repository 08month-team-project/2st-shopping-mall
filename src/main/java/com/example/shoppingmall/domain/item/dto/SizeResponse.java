package com.example.shoppingmall.domain.item.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SizeResponse {

    private List<SizeItem> sizeItemList;

}
