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
        // 비속어 목록 추가
    }

    public String filterBadWords(String text) {
        for (Map.Entry<String, String> entry : badWordsMap.entrySet()) {
            text = text.replaceAll(entry.getKey(), entry.getValue());
        }
        return text;
    }
}
