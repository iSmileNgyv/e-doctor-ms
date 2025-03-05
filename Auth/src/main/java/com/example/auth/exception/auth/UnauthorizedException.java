package com.example.auth.exception.auth;

import com.example.auth.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super("Unauthorized", HttpStatus.UNAUTHORIZED.value());
    }

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
