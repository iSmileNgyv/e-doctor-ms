package com.example.auth.service.impl;

import com.example.auth.repository.MessageRepository;
import com.example.auth.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    @Override
    public String getMessage(String code, String language) {
        var messageOpt = messageRepository.findByCode(code);
        return messageOpt.map(message -> message.getTranslations().getOrDefault(language, "Message not available"))
                .orElse("Message not found");    }
}
