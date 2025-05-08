package com.example.infrastructure.service.impl;

import com.example.infrastructure.entity.TranslationEntity;
import com.example.infrastructure.repository.TranslationRepository;
import com.example.infrastructure.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {
    private final TranslationRepository translationRepository;

    @Override
    public void addTranslation(String source, String code, Map<String, String> translations) {
        // Check if the translation already exists
        var existingTranslation = translationRepository.findByCodeAndSource(code, source);
        if (existingTranslation.isPresent()) {
            // Update the existing translation
            var translationEntity = existingTranslation.get();
            translationEntity.getTranslations().putAll(translations);
            translationRepository.save(translationEntity);
        } else {
            // Create a new translation
            var newTranslation = new TranslationEntity();
            newTranslation.setSource(source);
            newTranslation.setCode(code);
            newTranslation.setTranslations(translations);
            translationRepository.save(newTranslation);
        }
    }

    @Override
    public List<Map<String, String>> getTranslations(String source) {
        // Fetch all translations for the given source
        var translations = translationRepository.findBySource(source);
        return translations.stream()
                .map(TranslationEntity::getTranslations)
                .toList();
    }

    @Override
    public void updateTranslation(String source, String code, Map<String, String> translations) {
        var translation = translationRepository.findByCodeAndSource(code, source);
        if (translation.isPresent()) {
            var translationEntity = translation.get();
            translationEntity.getTranslations().putAll(translations);
            translationRepository.save(translationEntity);
        } else {
            throw new RuntimeException("Translation not found");
        }
    }
}
