package com.example.auth.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "messages")
public class MessageEntity {
    @Id
    private UUID id = java.util.UUID.randomUUID();

    @Indexed(unique = true)
    private String code;

    private Map<String, String> translations;
}

