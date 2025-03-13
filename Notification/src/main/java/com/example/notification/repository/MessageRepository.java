package com.example.notification.repository;

import com.example.notification.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends MongoRepository<MessageEntity, UUID> {
    Optional<MessageEntity> findByCode(String code);
}
