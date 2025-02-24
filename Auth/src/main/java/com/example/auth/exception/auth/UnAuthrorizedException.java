package com.example.auth.exception.auth;

import com.example.auth.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnAuthrorizedException extends BaseException {
    public UnAuthrorizedException() {
        super("Unauthorized", HttpStatus.UNAUTHORIZED.value());
    }

    public UnAuthrorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
