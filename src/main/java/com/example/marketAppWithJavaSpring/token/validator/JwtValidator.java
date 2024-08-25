package com.example.marketAppWithJavaSpring.token.validator;

import com.example.marketAppWithJavaSpring.token.JwtParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private final JwtParser jwtParser;
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = jwtParser.extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return jwtParser.extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
