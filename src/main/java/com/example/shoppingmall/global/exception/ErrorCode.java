package com.example.shoppingmall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //Auth,UserException
    USER_NOT_FOUND(NOT_FOUND,"사용자를 찾을 수 없습니다."),
    ALREADY_EXIST_USER(CONFLICT,"이미 존재하는 사용자입니다."),
    CREATE_USER_FAILED(INTERNAL_SERVER_ERROR,"회원가입에 실패했습니다."),

    //ItemException
    NOT_FOUND_ITEM(NOT_FOUND, "물품 및 해당 물품옵션을 찾을 수 없습니다."),
    CART_QUANTITY_EXCEEDS_STOCK(BAD_REQUEST, "재고보다 많을 수량을 담을 수 없습니다."),
    PRODUCT_NOT_FOR_SALE(NOT_FOUND, "이 상품은 현재 판매 중이 아닙니다."),

    //CartException
    CART_ITEM_NOT_MODIFIABLE(FORBIDDEN, "본인의 장바구니에 담긴 물품만 수정할 수 있습니다."),

    //S3Exception
    INVALID_IMAGE_TYPE(BAD_REQUEST, "허용되지 않는 파일 형식입니다. JPEG, JPG, PNG 파일만 가능합니다."),
    IMAGE_TOO_LARGE(PAYLOAD_TOO_LARGE, "파일 크기가 1MB를 초과합니다."),
    NOT_FOUND_IMAGE_URL(NOT_FOUND,"해당 사진의 주소가 존재 하지 않습니다."),
    NOT_VALID_URL(NOT_FOUND,"잘못된 URL 형태입니다."),
    NOT_ENOUGH_IMAGES(NOT_FOUND,"최소 1장의 이미지를 업로드해야 합니다."),
    MAX_UPLOAD_LIMIT(NOT_FOUND,"최대 3장 까지만 업로드가 가능합니다."),
    INVALID_URL_FORMAT(NOT_FOUND,"잘못된 URL 형식입니다.")

    ;

    private final HttpStatus httpStatus;
    private final String message;
}

