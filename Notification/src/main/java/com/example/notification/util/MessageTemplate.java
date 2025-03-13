package com.example.notification.util;

import com.example.notification.entity.MessageEntity;
import com.example.notification.repository.MessageRepository;
import com.example.notification.util.enums.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessageTemplate {
    private final MessageRepository messageRepository;
    public String getLocalizedMessage(ResponseMessage messageCode, String lang, Map<String, String> params) {
        MessageEntity messageEntity = messageRepository.findByCode(messageCode.toString()).orElse(null);
        if (messageEntity == null) return "Message not found!";

        String template = messageEntity.getTranslations().getOrDefault(lang, messageEntity.getTranslations().get("en"));

        for (Map.Entry<String, String> entry : params.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return template;
    }
}
