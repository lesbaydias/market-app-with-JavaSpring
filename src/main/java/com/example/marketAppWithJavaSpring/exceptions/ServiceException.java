package com.example.marketAppWithJavaSpring.exceptions;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class ServiceException extends RuntimeException{
    private final String message;
    private final HttpStatus status;

    public ServiceException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
