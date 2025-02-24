package com.example.auth.service.impl;

import com.example.auth.dto.auth.AuthResponseDto;
import com.example.auth.dto.register.RegisterRequestDto;
import com.example.auth.dto.register.RegisterResponseDto;
import com.example.auth.entity.RoleEntity;
import com.example.auth.entity.UserEntity;
import com.example.auth.exception.auth.UnAuthrorizedException;
import com.example.auth.exception.user.RoleNotFoundException;
import com.example.auth.exception.user.UsernameAlreadyExistException;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.AuthService;
import com.example.auth.util.TokenManager;
import lombok.RequiredArgsConstructor;
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

    @Override
    public RegisterResponseDto register(RegisterRequestDto request) {
        var isExist = userRepository.findByUsername(request.getUsername());
        if (isExist.isPresent()) {
            throw new UsernameAlreadyExistException();
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        Collection<RoleEntity> roleEntities = new ArrayList<>();
        request.getRoles().forEach(roleId -> {
            var role = roleRepository.findById(roleId)
                    .orElseThrow(RoleNotFoundException::new);
            roleEntities.add(role);
        });

        userEntity.setRoles(roleEntities);
        userEntity = userRepository.save(userEntity);

        return new RegisterResponseDto(userEntity.getId());
    }

    @Override
    public AuthResponseDto validateToken(String token) {
        if(tokenManager.isExpired(token)){
            throw new UnAuthrorizedException();
        }
        return new AuthResponseDto(tokenManager.getUsernameToken(token));
    }


}
