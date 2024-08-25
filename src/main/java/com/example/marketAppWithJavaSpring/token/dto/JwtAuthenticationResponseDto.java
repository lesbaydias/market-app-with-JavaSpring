package com.example.marketAppWithJavaSpring.token.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
public class JwtAuthenticationResponseDto {
    Date timestamp;
    String username;
    String accessToken;
    String refreshToken;
}
