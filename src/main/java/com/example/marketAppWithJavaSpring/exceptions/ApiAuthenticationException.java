package com.example.marketAppWithJavaSpring.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.naming.AuthenticationException;

@Getter
public class ApiAuthenticationException extends AuthenticationException {
    private final HttpStatus httpStatus;
    public ApiAuthenticationException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
