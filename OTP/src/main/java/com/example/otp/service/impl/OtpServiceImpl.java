package com.example.otp.service.impl;

import com.example.otp.dto.RequestDto;
import com.example.otp.dto.ResponseDto;
import com.example.otp.dto.verify.VerifyRequestDto;
import com.example.otp.exception.otp.CannotCreateOtpException;
import com.example.otp.exception.otp.UserNotFoundException;
import com.example.otp.exception.otp.WrongOtpException;
import com.example.otp.repository.UserRepository;
import com.example.otp.service.OtpService;
import com.example.otp.util.enums.DeliveryMethod;
import io.lettuce.core.RedisException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {
    private final UserRepository userRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    public ResponseDto createOtpCode(RequestDto request) {
        String key = request.getUserId() + "_" + request.getOperationType() + "_" + request.getRequestId();
        Random random = new Random();
        int value = random.nextInt(100000);
        try {
            stringRedisTemplate.opsForValue().set(key, Integer.toString(value), Duration.ofMinutes(5));
            var userRepositoryById = userRepository.findById(request.getUserId());
            if(userRepositoryById.isEmpty())
                throw new UserNotFoundException();
            var user = userRepositoryById.get();
            Map<String, Object> message = new HashMap<>();
            message.put("userId", user.getId());
            message.put("deliveryMethod", user.getDeliveryMethod());
            message.put("code", Integer.toString(value));
            kafkaTemplate.send("otp_" + user.getDeliveryMethod(), message);
        } catch (RedisException exception) {
            throw new CannotCreateOtpException();
        }
        return new ResponseDto(request.getRequestId());
    }

    @Override
    public ResponseDto verifyOtpCode(VerifyRequestDto request) {
        String key = request.getUserId() + "_" + request.getOperationType() + "_" + request.getRequestId();
        String value = request.getOtpCode();
        if(!stringRedisTemplate.hasKey(key) || stringRedisTemplate.opsForValue().get(key)== null) {
            throw new CannotCreateOtpException();
        }
        String storedValue = stringRedisTemplate.opsForValue().get(key);
        if(!value.equals(storedValue)) {
            throw new WrongOtpException();
        }
        return new ResponseDto(request.getRequestId());
    }
}
