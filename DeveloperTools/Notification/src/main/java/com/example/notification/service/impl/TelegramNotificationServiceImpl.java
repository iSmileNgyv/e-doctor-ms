package com.example.notification.service.impl;

import com.example.notification.service.NotificationService;
import com.example.notification.dto.BaseRequestDto;
import com.example.notification.dto.BaseResponseDto;
import org.springframework.stereotype.Service;

@Service
public class TelegramNotificationServiceImpl implements NotificationService<BaseRequestDto, BaseResponseDto> {

    @Override
    public BaseResponseDto send(BaseRequestDto request) {
        // TODO: implement TelegramNotificationServiceImpl logic
        return new BaseResponseDto();
    }
}