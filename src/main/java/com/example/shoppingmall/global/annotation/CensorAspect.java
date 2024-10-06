package com.example.shoppingmall.global.annotation;

import com.example.shoppingmall.domain.item.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;

@Aspect
@Component
@RequiredArgsConstructor
public class CensorAspect {

    private final CensorService censorService;

    @Before("execution(* com.example.shoppingmall..*(..)) && args(request)")
    public void censorFields(RegisterRequest request) throws IllegalAccessException {
        Field[] fields = RegisterRequest.class.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Censor.class)) {
                field.setAccessible(true);
                if (field.get(request) instanceof String) {
                    String originalValue = (String) field.get(request);
                    String censoredValue = censorService.filterBadWords(originalValue);
                    field.set(request, censoredValue);
                }
            }
        }
    }
}
