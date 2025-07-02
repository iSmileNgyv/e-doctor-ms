package com.example.auth.service.impl;

import com.example.auth.service.TranslationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TranslationServiceImpl implements TranslationService {
    private final Map<String, Map<String, String>> localTranslations = new ConcurrentHashMap<>();
    @Override
    public void saveTranslations(Map<String, Map<String, String>> translations) {
        localTranslations.clear();
        localTranslations.putAll(translations);
    }

    @Override
    public String get(String code, String lang) {
        return Optional.ofNullable(localTranslations.get(code))
                .map(m -> m.getOrDefault(lang, code))
                .orElse(code);
    }

    @KafkaListener(topics = "auth_translation", groupId = "auth_group")
    public void listen(Map<String, Map<String, String>> message) {
        saveTranslations(message);
    }
}
