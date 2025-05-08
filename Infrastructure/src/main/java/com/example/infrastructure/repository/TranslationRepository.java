package com.example.infrastructure.repository;

import com.example.infrastructure.entity.TranslationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface TranslationRepository extends MongoRepository<TranslationEntity, UUID> {
    Optional<TranslationEntity> findByCode(String code);
    List<TranslationEntity> findBySource(String source);
    Optional<TranslationEntity> findByCodeAndSource(String code, String source);
}
