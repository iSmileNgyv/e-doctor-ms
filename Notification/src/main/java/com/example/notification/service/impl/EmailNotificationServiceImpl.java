package com.example.notification.service.impl;

import com.example.notification.dto.EmailNotificationRequestDto;
import com.example.notification.dto.EmailNotificationResponseDto;
import com.example.notification.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationServiceImpl implements NotificationService<EmailNotificationRequestDto, EmailNotificationResponseDto> {

    @Override
    public EmailNotificationResponseDto send(EmailNotificationRequestDto request) {
        var response = new EmailNotificationResponseDto();
        response.setRequestId(request.getRequestId());
        return response;
    }
}
