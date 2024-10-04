package com.example.shoppingmall.global.security.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisAuthUtil {
    private final RedisTemplate<String,String> redisTemplate;

    public void saveRefreshToken(String email, String refreshToken){
        redisTemplate.opsForValue().set(email,refreshToken,3, TimeUnit.DAYS);
    }
    public String getRefreshToken(String email){
        return redisTemplate.opsForValue().get(email);
    }
    public void deleteRefreshToken(String email){
        redisTemplate.delete(email);
    }
}
