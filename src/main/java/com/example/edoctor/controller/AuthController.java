package com.example.edoctor.controller;

import com.example.edoctor.dto.auth.AuthResponseDTO;
import com.example.edoctor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("auth")
    public AuthResponseDTO auth(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        System.out.println("AuthController.auth " + token);
        return authService.auth(token);
    }
}
