package com.example.auth.exception.user;

import com.example.auth.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotFound extends BaseException {
    public UserNotFound(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }

    public UserNotFound() {
        super("User not found", HttpStatus.NOT_FOUND.value());
    }
}
