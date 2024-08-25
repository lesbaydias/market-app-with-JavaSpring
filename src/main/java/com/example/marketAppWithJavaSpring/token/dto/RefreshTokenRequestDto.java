package com.example.marketAppWithJavaSpring.token.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDto {
    private String refreshToken;
}
