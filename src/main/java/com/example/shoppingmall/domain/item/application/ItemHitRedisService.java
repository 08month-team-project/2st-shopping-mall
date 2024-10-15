package com.example.shoppingmall.domain.item.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemHitRedisService {

    private final RedisTemplate<String, Long> hitsTemplate;

    public boolean handleItemHit(Long itemId, HttpServletRequest request) {

        if (getHits(getKey(itemId, request)) == null) {
            saveHit(getKey(itemId, request));
            return true;
        }
        return increaseHit(getKey(itemId, request));
    }

    private void saveHit(String key) {
        hitsTemplate.opsForValue()
                .set(key, 1L, 1, TimeUnit.DAYS);
    }

    public boolean increaseHit(String key) {
        if (getHits(key) < 10) {
            hitsTemplate.opsForValue().increment(key, 1);
            return true;
        }
        return false;
    }

    private Long getHits(String key) {
        return hitsTemplate.opsForValue().get(key);
    }

    private String getKey(Long itemId, HttpServletRequest request) {
        return itemId + ":" + getHashedKey(request);
    }


    // 바로 get뭐시깽 하면 Ip6 로 나오기때문에 순회하면서 찾아야함
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // IPv6 로컬호스트 주소를 IPv4로 변환
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost(); // "127.0.0.1" 이 아닌 실제 ip 주소로 바꿈
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            ip = address.getHostAddress();
        }

        return ip;
    }

    private String getHashedKey(HttpServletRequest request) {

        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        log.info("ip={}, userAgent={}", ip, userAgent); // 원래는 debug로 돌려야함

        String value = ip + ":" + userAgent;

        // SHA-256 해시 사용
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] encodedHash = digest.digest(value.getBytes());

        // 해시 결과를 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return String.valueOf(hexString);
    }


}
