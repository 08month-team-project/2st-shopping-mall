package com.example.shoppingmall.global.exception;

import com.example.shoppingmall.domain.cart.excepction.CartException;
import com.example.shoppingmall.domain.item.excepction.ItemException;
import com.example.shoppingmall.domain.item.excepction.S3Exception;
import com.example.shoppingmall.domain.order.excepction.OrderErrorResult;
import com.example.shoppingmall.domain.order.excepction.OrderException;
import com.example.shoppingmall.domain.user.excepction.UserException;
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

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResult> handleUserException(UserException e){
        return makeErrorResult(e.getErrorCode());
    }

    @ExceptionHandler(ItemException.class)
    public ResponseEntity<ErrorResult> handleItemException(ItemException e){
        return makeErrorResult(e.getErrorCode());
    }

    @ExceptionHandler(CartException.class)
    public ResponseEntity<ErrorResult> handleCartException(CartException e){
        return makeErrorResult(e.getErrorCode());
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ErrorResult> handleS3Exception(S3Exception e){
        return makeErrorResult(e.getErrorCode());
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResult> handleOrderException(OrderException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new OrderErrorResult(e.getErrorCode(), e.getOrderItemResponse()));
    }

    private ResponseEntity<ErrorResult> makeErrorResult(ErrorCode errorCode){
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ErrorResult(errorCode));
    }

}
