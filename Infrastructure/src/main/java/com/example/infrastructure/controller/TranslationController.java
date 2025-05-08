package com.example.infrastructure.controller;

import com.example.infrastructure.dto.translation.AddTranslationRequestDto;
import com.example.infrastructure.dto.translation.UpdateTranslationRequestDto;
import com.example.infrastructure.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/translation")
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @PostMapping
    public void addTranslation(@RequestBody AddTranslationRequestDto request) {
        translationService.addTranslation(request.getSource(), request.getCode(), request.getTranslations());
    }

    @GetMapping("/{source}")
    public List<Map<String, String>> getTranslations(@PathVariable String source) {
        return translationService.getTranslations(source.toLowerCase());
    }

    @PutMapping("/edit")
    public void updateTranslation(@RequestBody UpdateTranslationRequestDto request) {
        translationService.updateTranslation(request.getSource(), request.getCode(), request.getTranslations());
    }
}
