package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.dto.LoginDto;
import com.example.marketAppWithJavaSpring.dto.RegisterDto;
import com.example.marketAppWithJavaSpring.token.dto.JwtAuthenticationResponseDto;
import com.example.marketAppWithJavaSpring.token.dto.RefreshTokenRequestDto;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository {
    RegisterDto register(RegisterDto registerDto) ;
    JwtAuthenticationResponseDto login(LoginDto loginDto) throws Exception;
    JwtAuthenticationResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequest);
}
