package com.example.otp.exception.otp;

import com.example.otp.exception.BaseException;
import org.springframework.http.HttpStatus;

public class WrongOtpException extends BaseException {
    public WrongOtpException(String message) {
      super(message, HttpStatus.BAD_REQUEST.value());
    }

    public WrongOtpException() {
        super("OTP is wrong", HttpStatus.BAD_REQUEST.value());
    }
}
