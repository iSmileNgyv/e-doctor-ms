package com.example.notification.service.impl;

import com.example.notification.service.NotificationService;
import com.example.notification.dto.{{REQUEST_DTO}};
import com.example.notification.dto.{{RESPONSE_DTO}};
import org.springframework.stereotype.Service;

@Service
public class {{CLASS_NAME}} implements NotificationService<{{REQUEST_DTO}}, {{RESPONSE_DTO}}> {

    @Override
    public {{RESPONSE_DTO}} send({{REQUEST_DTO}} request) {
        // TODO: implement {{CLASS_NAME}} logic
        return new {{RESPONSE_DTO}}();
    }
}