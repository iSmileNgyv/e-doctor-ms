package com.example.otp.service.impl;

import com.example.otp.dto.RequestDto;
import com.example.otp.dto.register.RegisterRequestDto;
import com.example.otp.dto.verify.VerifyRequestDto;
import com.example.otp.entity.UserEntity;
import com.example.otp.exception.otp.CannotCreateOtpException;
import com.example.otp.exception.otp.UserIsExistException;
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
    public void createOtpCode(RequestDto request, String userAgent) {
        String key = request.getUserId() + "_" + request.getOperationType() + "_" + userAgent;
        Random random = new Random();
        int value = random.nextInt(100000);
        try {
            stringRedisTemplate.opsForValue().set(key, Integer.toString(value), Duration.ofMinutes(5));
            var userRepositoryById = userRepository.findByUserId(request.getUserId());
            if(userRepositoryById.isEmpty())
                throw new UserNotFoundException();
            var user = userRepositoryById.get();
            Map<String, Object> message = new HashMap<>();
            message.put("userId", user.getUserId());
            message.put("deliveryMethod", user.getDeliveryMethod());
            message.put("code", Integer.toString(value));
            kafkaTemplate.send("otp_" + user.getDeliveryMethod(), message);
        } catch (RedisException exception) {
            throw new CannotCreateOtpException();
        }
    }

    @Override
    public void verifyOtpCode(VerifyRequestDto request, String userAgent) {
        String key = request.getUserId() + "_" + request.getOperationType() + "_" + userAgent;
        String value = request.getOtpCode();
        if(!stringRedisTemplate.hasKey(key) || stringRedisTemplate.opsForValue().get(key)== null) {
            throw new CannotCreateOtpException();
        }
        String storedValue = stringRedisTemplate.opsForValue().get(key);
        if(!value.equals(storedValue)) {
            throw new WrongOtpException();
        }
    }

    @Override
    public void register(RegisterRequestDto request) {
        var user = userRepository.findByUserId(request.getUserId());
        if(user.isPresent())
            throw new UserIsExistException();
        var userEntity = new UserEntity();
        userEntity.setUserId(request.getUserId());
        userEntity.setDeliveryMethod(DeliveryMethod.valueOf(request.getDeliveryMethod().name()));
        userRepository.save(userEntity);
    }
}
