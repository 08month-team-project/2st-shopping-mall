package com.example.shoppingmall.global.annotation;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CensorService {
    private static final Map<String, String> badWordsMap = new HashMap<>();

    static {
        badWordsMap.put("존나", "****");
        badWordsMap.put("개같은", "****");
        badWordsMap.put("년", "****");
        badWordsMap.put("씹","****");
        badWordsMap.put("창","****");
        // 추가 비속어 정의
    }

    public boolean containsBadWords(String input) {
        for (String badWord : badWordsMap.keySet()) {
            if (input.contains(badWord)) {
                return true; // 비속어가 포함되어 있다면 true 반환
            }
        }
        return false; // 비속어가 없으면 false 반환
    }
}
