package com.example.notification.exception.notification;

import com.example.notification.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }

    public UserNotFoundException() {
        super("User not found", HttpStatus.NOT_FOUND.value());
    }
}
