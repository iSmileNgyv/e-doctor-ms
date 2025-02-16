package com.example.notification.service;

import com.example.notification.dto.BaseRequestDto;
import com.example.notification.dto.BaseResponseDto;

public interface NotificationService<T extends BaseRequestDto, R extends BaseResponseDto> {
    R send(T request);
}
