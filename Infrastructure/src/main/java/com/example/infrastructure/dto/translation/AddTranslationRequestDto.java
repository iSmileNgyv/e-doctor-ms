package com.example.infrastructure.dto.translation;

import lombok.Data;

import java.util.Map;

@Data
public class AddTranslationRequestDto {
    private String source;
    private String code;
    private Map<String, String> translations;
}
