package com.example.infrastructure.dto.translation;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateTranslationRequestDto {
    private String source;
    private String code;
    private Map<String, String> translations;
}
