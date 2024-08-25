package com.example.marketAppWithJavaSpring.config;

import com.example.marketAppWithJavaSpring.config.properties.MinioProperties;
import com.example.marketAppWithJavaSpring.model.Seller;
import com.example.marketAppWithJavaSpring.model.User;
import com.example.marketAppWithJavaSpring.service.SellerService;
import com.example.marketAppWithJavaSpring.service.UserService;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final UserService userService;
    private final MinioProperties minioProperties;
    private final SellerService sellerService;
    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Seller seller = sellerService
                    .findByUsernameWithoutThrow(username)
                    .orElse(null);

            User user = userService
                    .findByUsernameWithoutThrow(username)
                    .orElse(null);

            return seller == null ? user : seller;
        };
    }
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                              PasswordEncoder passwordEncoder){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
