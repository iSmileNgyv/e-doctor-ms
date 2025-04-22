package com.example.auth.service;

import com.example.auth.dto.user.GetAllUserResponseDto;

import java.util.List;

public interface UserService {
    List<GetAllUserResponseDto> getAllUsers();
}
