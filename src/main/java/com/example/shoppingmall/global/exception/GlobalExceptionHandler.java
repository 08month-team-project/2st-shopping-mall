package com.example.shoppingmall.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 우리가 미처 잡지 못한 에러 처리용
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        log.error("[Exception] ex", e);
        return ResponseEntity.internalServerError().body("서버에 문제가 발생했습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] {}", FieldErrorCustom.getFieldErrorList(e.getFieldErrors()));
        return ResponseEntity.badRequest().body(new ErrorResult(e));
    }

}
