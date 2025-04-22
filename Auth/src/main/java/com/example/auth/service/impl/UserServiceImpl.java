package com.example.auth.service.impl;

import com.example.auth.dto.user.GetAllUserResponseDto;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<GetAllUserResponseDto> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(user -> {
                    List<GetAllUserResponseDto.RoleDto> roleDtos = user.getRoles().stream()
                            .map(role -> new GetAllUserResponseDto.RoleDto(role.getCode(), role.getName()))
                            .toList();

                    return GetAllUserResponseDto.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .username(user.getUsername())
                            .isActive(user.isActive())
                            .phoneNumber(user.getPhoneNumber())
                            .roles(roleDtos)
                            .build();
                })
                .toList();
    }
}
