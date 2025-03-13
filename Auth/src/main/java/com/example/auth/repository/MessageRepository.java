package com.example.auth.repository;

import com.example.auth.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends MongoRepository<MessageEntity, UUID> {
    Optional<MessageEntity> findByCode(String code);
}
