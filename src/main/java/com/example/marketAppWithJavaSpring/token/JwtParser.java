package com.example.marketAppWithJavaSpring.token;

import com.example.marketAppWithJavaSpring.token.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtParser {
    private final JwtService jwtService;
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token){
            return Jwts.parser()
                    .setSigningKey(jwtService.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
    }
}
