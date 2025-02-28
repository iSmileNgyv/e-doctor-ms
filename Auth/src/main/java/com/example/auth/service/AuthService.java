package com.example.auth.service;

import com.example.auth.dto.auth.AuthResponseDto;
import com.example.auth.dto.register.RegisterRequestDto;
import com.example.auth.dto.register.RegisterResponseDto;

public interface AuthService {
    RegisterResponseDto register(RegisterRequestDto request);
    //AuthResponseDto validateToken(String token);

}
