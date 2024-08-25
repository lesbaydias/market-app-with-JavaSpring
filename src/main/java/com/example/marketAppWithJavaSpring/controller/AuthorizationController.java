package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.dto.LoginDto;
import com.example.marketAppWithJavaSpring.dto.RegisterDto;
import com.example.marketAppWithJavaSpring.repository.AuthenticationRepository;
import com.example.marketAppWithJavaSpring.token.dto.JwtAuthenticationResponseDto;
import com.example.marketAppWithJavaSpring.token.dto.RefreshTokenRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthorizationController {
    private final AuthenticationRepository authenticationRepository;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RegisterDto> register(@RequestBody RegisterDto registerDto){
        return ResponseEntity.ok(authenticationRepository.register(registerDto));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<JwtAuthenticationResponseDto> login(@RequestBody LoginDto loginDto) throws Exception {
        return ResponseEntity.ok(authenticationRepository.login(loginDto));
    }


    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JwtAuthenticationResponseDto> refresh(
            @RequestBody RefreshTokenRequestDto refreshTokenRequest){
        return ResponseEntity.ok(authenticationRepository.refreshToken(refreshTokenRequest));
    }
}
