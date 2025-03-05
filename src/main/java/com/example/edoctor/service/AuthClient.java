package com.example.edoctor.service;

import com.example.edoctor.AuthRequestDto;
import com.example.edoctor.AuthResponseDto;
import com.example.edoctor.AuthServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class AuthClient {
    @GrpcClient("authService")
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    public AuthResponseDto authenticate(String token) {
        return this.authServiceBlockingStub.auth(AuthRequestDto.newBuilder()
                .setToken(token)
                .build()
        );
    }
}
