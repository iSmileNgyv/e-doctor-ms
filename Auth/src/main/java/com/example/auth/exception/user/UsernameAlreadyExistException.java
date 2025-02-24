package com.example.auth.exception.user;

import com.example.auth.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistException extends BaseException {
    public UsernameAlreadyExistException() {
        super("Username already exist", HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
