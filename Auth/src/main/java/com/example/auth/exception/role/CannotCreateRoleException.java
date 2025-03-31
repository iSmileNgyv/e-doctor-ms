package com.example.auth.exception.role;

import com.example.auth.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CannotCreateRoleException extends BaseException {
    public CannotCreateRoleException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    public CannotCreateRoleException() {
      super("Cannot create role", HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
