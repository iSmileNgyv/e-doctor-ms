package com.example.auth.service;

import com.example.auth.repository.MessageRepository;

public interface MessageService {
    String getMessage(String code, String language);
}
