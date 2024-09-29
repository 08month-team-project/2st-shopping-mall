package com.example.shoppingmall.global.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@ToString
public class FieldErrorCustom {

    private String field;

    private Object rejectedValue;

    private String message;

    public FieldErrorCustom(FieldError fieldError) {
        this.field = fieldError.getField();
        this.rejectedValue = fieldError.getRejectedValue();
        this.message = fieldError.getDefaultMessage();
    }

    public static List<FieldErrorCustom> getFieldErrorList(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(FieldErrorCustom::new)
                .collect(Collectors.toList());
    }
}
