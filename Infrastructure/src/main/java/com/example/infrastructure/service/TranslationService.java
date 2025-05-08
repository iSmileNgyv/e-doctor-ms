package com.example.infrastructure.service;

import java.util.Map;
import java.util.List;

public interface TranslationService {
    void addTranslation(String source, String code, Map<String, String> translations);
    List<Map<String, String>> getTranslations(String source);
    //update
    void updateTranslation(String source, String code, Map<String, String> translations);
}
