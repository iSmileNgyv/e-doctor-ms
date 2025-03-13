package com.example.auth.service.impl;

import com.example.auth.AuthRequestDto;
import com.example.auth.AuthResponseDto;
import com.example.auth.AuthServiceGrpc;
import com.example.auth.exception.auth.UnauthorizedException;
import com.example.auth.util.TokenManager;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AuthGrpcServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {
    private final TokenManager tokenManager;
    @Override
    public void auth(AuthRequestDto request, StreamObserver<AuthResponseDto> responseObserver) {
        String token = request.getToken();
        if(token.isEmpty()) {
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Token is empty").asRuntimeException());
            return;
        }
        try {
            tokenManager.isExpired(token);
            System.err.println(token);
            responseObserver.onNext(
                    AuthResponseDto.newBuilder()
                            .setUsername(tokenManager.getUsernameToken(token))
                            .setExpiredAt(tokenManager.getExpiredAt(token))
                            .build()
            );
        } catch (UnauthorizedException e) {
            responseObserver.onError(Status.UNAUTHENTICATED.withDescription("Token is invalid").asRuntimeException());
            return;
        }
        responseObserver.onCompleted();
    }
}