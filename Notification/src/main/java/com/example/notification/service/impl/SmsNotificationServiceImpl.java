package com.example.notification.service.impl;

import com.example.notification.dto.BaseRequestDto;
import com.example.notification.dto.BaseResponseDto;
import com.example.notification.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationServiceImpl implements NotificationService<BaseRequestDto, BaseResponseDto> {
    @Override
    public BaseResponseDto send(BaseRequestDto request) {
        return new BaseResponseDto(request.getRequestId());
    }
}
