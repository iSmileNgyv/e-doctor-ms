package com.example.notification.service.impl;

import com.example.notification.dto.EmailNotificationRequestDto;
import com.example.notification.dto.EmailNotificationResponseDto;
import com.example.notification.exception.notification.UserNotFoundException;
import com.example.notification.repository.UserRepository;
import com.example.notification.service.NotificationService;
import com.example.notification.util.enums.DeliveryMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements NotificationService<EmailNotificationRequestDto, EmailNotificationResponseDto> {
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    @Override
    public EmailNotificationResponseDto send(EmailNotificationRequestDto request) {
        var response = new EmailNotificationResponseDto();
        response.setRequestId(request.getRequestId());
        this.sendEmail(request.getRecipient(), request.getSubject(), request.getMessage());
        return response;
    }

    private void sendEmail(String setTo, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(setTo);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("ismayilnagiyev100@gmail.com");
        javaMailSender.send(message);
    }

    @KafkaListener(topics = "otp_1", groupId = "notification_group")
    private void listenForOtpNotification(Map<String, Object> message, Acknowledgment acknowledgment) {
        try {
            EmailNotificationRequestDto request = new EmailNotificationRequestDto();
            var findUser = userRepository.findByUserIdAndDeliveryMethod((int) message.get("userId"),DeliveryMethod.fromValue((Integer) message.get("deliveryMethod")));
            if(findUser.isEmpty())
                throw new UserNotFoundException();
            var user = findUser.get();
            request.setSubject("Your otp code");
            request.setRecipient(user.getDeliverySetting());
            request.setMessage(message.get("code").toString());
            this.send(request);
            acknowledgment.acknowledge();
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
