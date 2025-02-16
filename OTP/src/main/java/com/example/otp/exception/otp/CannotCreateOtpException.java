package com.example.otp.exception.otp;

import com.example.otp.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CannotCreateOtpException extends BaseException {
    public CannotCreateOtpException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    public CannotCreateOtpException() {
        super("Sending otp is failed", HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
