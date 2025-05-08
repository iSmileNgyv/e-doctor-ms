package com.example.infrastructure.dto.translation;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GetTranslationsBySourceResponseDto {
    private String code;
    private List<Map<String, String>> translations;
}
