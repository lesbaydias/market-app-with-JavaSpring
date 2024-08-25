package com.example.marketAppWithJavaSpring.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(ServiceException ex, WebRequest request) {
        request.getHeaderNames();

        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), ex.getStatus());
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> ExpiredJwtException(ServiceException ex, WebRequest request) {
        request.getHeaderNames();

        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage(), ex.getStatus());
        return new ResponseEntity<>(errorMessage,new HttpHeaders(), ex.getStatus());
    }

    @Getter
    @RequiredArgsConstructor
    private static class ErrorMessage {
        private final Date timestamp;
        private final String message;
        private final HttpStatus status;
    }
}
