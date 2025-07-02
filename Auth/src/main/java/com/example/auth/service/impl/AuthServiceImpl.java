package com.example.auth.service.impl;

import com.example.auth.CreateOtpRequestDto;
import com.example.auth.dto.login.LoginRequestDto;
import com.example.auth.dto.login.LoginResponseDto;
import com.example.auth.dto.otp.VerifyOtpRequestDto;
import com.example.auth.dto.register.RegisterRequestDto;
import com.example.auth.entity.RoleEntity;
import com.example.auth.entity.UserEntity;
import com.example.auth.exception.auth.UnauthorizedException;
import com.example.auth.exception.user.UserNotFound;
import com.example.auth.exception.user.UsernameAlreadyExistException;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.AuthService;
import com.example.auth.service.TranslationService;
import com.example.auth.util.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final RegisterGrpcClientImpl registerGrpcClient;
    private final OtpGrpcClientServiceImpl otpGrpcClientService;
    private final TranslationService translationService;

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
        var role = roleRepository.findByDefaultRole(true);
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
    public LoginResponseDto login(LoginRequestDto request, String userAgent) {
        var userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(
                        () -> new UnauthorizedException(translationService.get("UNAUTHORIZED_OPERATION", "en"))
                );
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new UserNotFound(translationService.get("WRONG_AUTH", "en"));
        }

        if(userEntity.isLoginOtp()) {
            otpGrpcClientService.createOtp(CreateOtpRequestDto.newBuilder()
                            .setUserId(userEntity.getId())
                            .setOperationType(com.example.auth.OperationType.LOGIN)
                            .setUserAgent(userAgent)
                    .build());
            return new LoginResponseDto();
        }
        var roles = new ArrayList<String>();
        userEntity.getRoles().forEach(roleEntity -> {
            roles.add(roleEntity.getCode());
        });
        return new LoginResponseDto(tokenManager.generateToken(userEntity.getUsername(), roles, userEntity.isSuperAdmin()));
    }

    @Override
    public LoginResponseDto verifyLoginOtp(VerifyOtpRequestDto request, String userAgent) {
        var userEntity = userRepository.findById(request.getUserId())
                .orElseThrow(UnauthorizedException::new);
        try {
            otpGrpcClientService.verifyOtp(com.example.auth.VerifyOtpRequestDto.newBuilder()
                            .setOperationType(com.example.auth.OperationType.LOGIN)
                            .setOtpCode(request.getOtpCode())
                            .setUserId(request.getUserId())
                            .setUserAgent(userAgent)
                    .build());
            var roles = new ArrayList<String>();
            userEntity.getRoles().forEach(roleEntity -> {
                roles.add(roleEntity.getCode());
            });
            return new LoginResponseDto(tokenManager.generateToken(userEntity.getUsername(), roles, userEntity.isSuperAdmin()));
        } catch(Exception e) {
            throw new UnauthorizedException(translationService.get("INVALID_OTP", "en"));
        }
    }

    @Override
    public void verifyRegisterOtp(VerifyOtpRequestDto request, String userAgent) {
        var entity = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFound::new);
        try {
            otpGrpcClientService.verifyOtp(com.example.auth.VerifyOtpRequestDto.newBuilder()
                            .setOperationType(com.example.auth.OperationType.REGISTER)
                            .setUserId(entity.getId())
                            .setUserAgent(userAgent)
                            .setOtpCode(request.getOtpCode())
                    .build());
        } catch(Exception e) {
            return;
        }
        entity.setActive(true);
        userRepository.save(entity);
    }

}
