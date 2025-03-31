package com.example.auth.exception.role;

import com.example.auth.exception.BaseException;
import org.springframework.http.HttpStatus;

public class CannotUpdateRoleException extends BaseException {
    public CannotUpdateRoleException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    public CannotUpdateRoleException() {
        super("Cannot update role", HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
