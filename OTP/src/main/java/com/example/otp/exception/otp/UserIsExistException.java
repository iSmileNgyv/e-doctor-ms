package com.example.otp.exception.otp;

import com.example.otp.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserIsExistException extends BaseException {
    public UserIsExistException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }

    public UserIsExistException() {
        this("User already exists.");
    }
}
