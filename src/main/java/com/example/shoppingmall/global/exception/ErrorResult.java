package com.example.shoppingmall.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static com.example.shoppingmall.global.exception.FieldErrorCustom.getFieldErrorList;

/**
 * 커스텀 예외 반환용 클래스
 */
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResult {
    private String code;
    private int httpStatus;
    private String message;


    private List<FieldErrorCustom> fieldErrors;

    public ErrorResult(String code, int httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ErrorResult(ErrorCode errorCode) {
        this.code = String.valueOf(errorCode.getHttpStatus());
        this.httpStatus = errorCode.getHttpStatus().value();
        this.message = errorCode.getMessage();
    }

    public ErrorResult(MethodArgumentNotValidException e) {
        this.code = String.valueOf(HttpStatus.BAD_REQUEST);
        this.httpStatus = HttpStatus.BAD_REQUEST.value();
        this.fieldErrors = getFieldErrorList(e.getFieldErrors());
    }
}
