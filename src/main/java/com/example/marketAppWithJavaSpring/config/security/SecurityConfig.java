package com.example.marketAppWithJavaSpring.config.security;

import com.example.marketAppWithJavaSpring.enums.TypeOfUser;
import com.example.marketAppWithJavaSpring.token.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
public class SecurityConfig{
    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAnyAuthority(TypeOfUser.ADMIN.name())
                .antMatchers(
                        "/user/**",
                        "/order/**",
                        "/reviews/**",
                        "/payments/**",
                        "/card/**",
                        "/credit/**",
                        "/basket/**").hasAuthority(TypeOfUser.REGISTERED_USER.name())
                .antMatchers(
                        "/sellers/**").hasAnyAuthority(TypeOfUser.SELLER.name())
                .antMatchers(
                        "/auth/**",
                        "/swagger-ui/**",
                        "/products/**",
                        "/category/**")
                .permitAll()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider).addFilterBefore(
                        jwtRequestFilter, UsernamePasswordAuthenticationFilter.class
                );
        return httpSecurity.build();
    }

}
