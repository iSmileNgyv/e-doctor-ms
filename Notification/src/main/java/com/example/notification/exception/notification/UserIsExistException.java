package com.example.notification.exception.notification;

import com.example.notification.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserIsExistException extends BaseException {
    public UserIsExistException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }

    public UserIsExistException() {
        super("User is exist", HttpStatus.CONFLICT.value());
    }
}
