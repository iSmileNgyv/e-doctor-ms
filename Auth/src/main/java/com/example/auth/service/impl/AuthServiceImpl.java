package com.example.auth.service.impl;

import com.example.auth.dto.login.LoginRequestDto;
import com.example.auth.dto.login.LoginResponseDto;
import com.example.auth.dto.otp.OtpRequestDto;
import com.example.auth.dto.otp.OtpResponseDto;
import com.example.auth.dto.otp.VerifyOtpRequestDto;
import com.example.auth.dto.register.RegisterRequestDto;
import com.example.auth.entity.RoleEntity;
import com.example.auth.entity.UserEntity;
import com.example.auth.exception.auth.UnauthorizedException;
import com.example.auth.exception.user.RoleNotFoundException;
import com.example.auth.exception.user.UserNotFound;
import com.example.auth.exception.user.UsernameAlreadyExistException;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.AuthService;
import com.example.auth.service.MessageService;
import com.example.auth.util.TokenManager;
import com.example.auth.util.enums.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenManager tokenManager;
    private final AuthenticationManager authenticationManager;
    private final WebClient webClient;
    private final RegisterGrpcClientImpl registerGrpcClient;
    private final MessageService messageService;

    @Override
    public void register(RegisterRequestDto request, String userAgent) {
        var isExist = userRepository.findByUsername(request.getUsername());
        if (isExist.isPresent()) {
            throw new UsernameAlreadyExistException();
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setActive(false); // need to verify otp

        Collection<RoleEntity> roleEntities = new ArrayList<>();
        // need field default role (USER)
        var role = roleRepository.findById(1)
                .orElseThrow(RoleNotFoundException::new);
        roleEntities.add(role);

        userEntity.setRoles(roleEntities);
        userRepository.save(userEntity);
        try {
            registerGrpcClient.registerMulti(userEntity.getId(), request.getEmail(), userAgent);
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public LoginResponseDto login(LoginRequestDto request, String requestId) {
        var userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new UnauthorizedException(messageService.getMessage("UNAUTHORIZED", "az"))
                );
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new UserNotFound(messageService.getMessage("EMAIL_PASSWORD_WRONG", "az"));
        }


        if(userEntity.isLoginOtp()) {
            var otpRequest = new OtpRequestDto(
                    OperationType.LOGIN,
                    userEntity.getId()
            );
            webClient.post()
                    .uri("http://localhost:8081/api/v1/otp")
                    .bodyValue(otpRequest)
                    .header("X-Request-Id", requestId)
                    .retrieve()
                    .bodyToMono(OtpResponseDto.class)
                    .block();
            return new LoginResponseDto();
        }
        return new LoginResponseDto(tokenManager.generateToken(request.getUsername()));
    }

    @Override
    public LoginResponseDto verifyLoginOtp(VerifyOtpRequestDto request) {
        var userEntity = userRepository.findById(request.getUserId())
                .orElseThrow(UnauthorizedException::new);
        try {
            request.setOperationType(OperationType.LOGIN);
            webClient.post()
                    .uri("http://localhost:8081/api/v1/otp/verify")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return new LoginResponseDto(tokenManager.generateToken(userEntity.getUsername()));
        } catch(Exception e) {
            throw new UnauthorizedException("Invalid OTP");
        }
    }

    @Override
    public void verifyRegisterOtp(VerifyOtpRequestDto request) {
        var entity = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFound::new);
        request.setOperationType(OperationType.REGISTER);
        webClient.post()
                .uri("http://localhost:8081/api/v1/otp/verify")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        entity.setActive(true);
        userRepository.save(entity);
    }

}
