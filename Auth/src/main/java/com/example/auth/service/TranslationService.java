package com.example.auth.service;

import java.util.Map;

public interface TranslationService {
    void saveTranslations(Map<String, Map<String, String>> translations);
    String get(String code, String lang);
}
