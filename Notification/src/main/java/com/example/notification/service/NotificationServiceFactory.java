package com.example.notification.service;

import com.example.notification.dto.BaseRequestDto;
import com.example.notification.dto.BaseResponseDto;
import com.example.notification.service.impl.EmailNotificationServiceImpl;
import com.example.notification.service.impl.SmsNotificationServiceImpl;
import com.example.notification.util.enums.DeliveryMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class NotificationServiceFactory {
    private final Map<DeliveryMethod, NotificationService<?, ?>> serviceMap;
    public NotificationServiceFactory() {
        serviceMap = new HashMap<>();
        serviceMap.put(DeliveryMethod.EMAIL, new EmailNotificationServiceImpl());
        serviceMap.put(DeliveryMethod.SMS, new SmsNotificationServiceImpl());
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseRequestDto, R extends BaseResponseDto>NotificationService<T, R> getService(DeliveryMethod method) {
        NotificationService<?, ?> service = serviceMap.get(method);
        if (service == null) {
            throw new IllegalArgumentException("Unsupported method: " + method);
        }

        return (NotificationService<T, R>) service;
    }
}
