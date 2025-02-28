package com.example.edoctor.exception.auth;


import com.example.edoctor.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnAuthrorizedException extends BaseException {
    public UnAuthrorizedException() {
        super("Unauthorized", HttpStatus.UNAUTHORIZED.value());
    }

    public UnAuthrorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
