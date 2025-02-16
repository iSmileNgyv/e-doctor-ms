package com.example.notification.controller;

import com.example.notification.dto.BaseRequestDto;
import com.example.notification.dto.BaseResponseDto;
import com.example.notification.dto.EmailNotificationRequestDto;
import com.example.notification.dto.EmailNotificationResponseDto;
import com.example.notification.service.NotificationService;
import com.example.notification.service.NotificationServiceFactory;
import com.example.notification.util.enums.DeliveryMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServiceFactory notificationServiceFactory;
    @PostMapping("sms")
    public BaseResponseDto send(@RequestBody BaseRequestDto request) {
        NotificationService<BaseRequestDto, BaseResponseDto> notificationService =  notificationServiceFactory.getService(DeliveryMethod.SMS);
        return notificationService.send(request);
    }

    @PostMapping("email")
    public EmailNotificationResponseDto send(@RequestBody EmailNotificationRequestDto request) {
        NotificationService<EmailNotificationRequestDto, EmailNotificationResponseDto> notificationService =  notificationServiceFactory.getService(DeliveryMethod.EMAIL);
        return notificationService.send(request);
    }
}
