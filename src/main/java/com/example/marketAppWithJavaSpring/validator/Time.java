package com.example.marketAppWithJavaSpring.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public String dateNow(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        return currentDateTime.format(FORMATTER);
    }
    public String expirationDate(){
        LocalDateTime currentDateTime = LocalDateTime.now().plusYears(4);

        return currentDateTime.format(FORMATTER);
    }
    public String creditEndDate(){
        LocalDateTime currentDateTime = LocalDateTime.now().plusMonths(3);
        return currentDateTime.format(FORMATTER);
    }
    public String saleEndDate(){
        LocalDateTime currentDateTime = LocalDateTime.now().plusDays(5);
        return currentDateTime.format(FORMATTER);
    }
}
