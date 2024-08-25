package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.dto.LoginDto;
import com.example.marketAppWithJavaSpring.dto.RegisterDto;
import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.enums.TypeOfUser;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Basket;
import com.example.marketAppWithJavaSpring.model.Seller;
import com.example.marketAppWithJavaSpring.model.User;
import com.example.marketAppWithJavaSpring.repository.AuthenticationRepository;
import com.example.marketAppWithJavaSpring.token.JwtParser;
import com.example.marketAppWithJavaSpring.token.dto.JwtAuthenticationResponseDto;
import com.example.marketAppWithJavaSpring.token.dto.RefreshTokenRequestDto;
import com.example.marketAppWithJavaSpring.token.repository.JwtRepository;
import com.example.marketAppWithJavaSpring.validator.SellerValidator;
import com.example.marketAppWithJavaSpring.validator.UserValidator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationRepository {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtRepository JwtRepository;
    private final JwtParser jwtParser;
    private final BasketService basketService;
    private final CardService cardService;
    private final SellerService sellerService;
    private final UserService userService;
    private final UserValidator userValidator;
    private final SellerValidator sellerValidator;

    private static final String SELLER_ROLE = "SELLER";
    private static final String REGISTERED_USER_ROLE = "REGISTERED_USER";
    private static final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(0);


    @Override
    @Transactional
    public RegisterDto register(RegisterDto registerDto) {
        log.info("Received registration request for username: {}", registerDto.getUsername());
        switch (registerDto.getRole().toUpperCase()){
            case SELLER_ROLE:
                registerSeller(registerDto);
                break;


            case REGISTERED_USER_ROLE:
                registerUser(registerDto);
                break;

            default:
                throw new ServiceException(
                        String.format(ErrorMessage.ROLE_IS_NOT_FOUND.getMessage(), registerDto.getRole()),
                        ErrorMessage.ROLE_IS_NOT_FOUND.getStatus()
                );
        }
        return registerDto;
    }
    private void registerSeller(RegisterDto registerDto) {
        sellerValidator.validate(registerDto);

        Seller seller = Seller.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .phoneNumber(registerDto.getPhoneNumber())
                .sellerName(registerDto.getFirstname() + " " + registerDto.getLastname())
                .status(TypeOfUser.SELLER)
                .address(registerDto.getAddress())
                .balance(DEFAULT_BALANCE)
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();

        sellerService.save(seller);

        log.info("Seller registered successfully with username: {}", seller.getUsername());
    }

    private void registerUser(RegisterDto registerDto) {
        userValidator.validate(registerDto);

        User user = User.builder()
                .username(registerDto.getUsername())
                .email(registerDto.getEmail())
                .phoneNumber(registerDto.getPhoneNumber())
                .firstname(registerDto.getFirstname())
                .lastname(registerDto.getLastname())
                .status(TypeOfUser.REGISTERED_USER)
                .address(registerDto.getAddress())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .build();

        userService.save(user);

        Basket bt = basketService.createBasket(user);
        basketService.save(bt); // Create a basket for a user

        log.info("Successfully created basket for {}", user.getUsername());

        cardService.save(user); // Create a card for a user

        log.info("Successfully created card for {}", user.getUsername());
        log.info("User registered successfully with username: {}", user.getUsername());
    }



    @Override
    public JwtAuthenticationResponseDto login(LoginDto loginDto){
        log.info("Received authentication request for username: {}", loginDto.getUsername());
        UserDetails userOrSeller = getUserDetails(loginDto.getUsername());

        if(!passwordEncoder.matches(loginDto.getPassword(), userOrSeller.getPassword()))
            throw new ServiceException(
                    String.format(ErrorMessage.PASSWORD_IS_WRONG.getMessage(), loginDto.getPassword()),
                    ErrorMessage.PASSWORD_IS_WRONG.getStatus()
            );

        String jwt = JwtRepository.generateToken(userOrSeller);
        String refreshToken = JwtRepository.generateRefreshToken(new HashMap<>(), userOrSeller);

        authenticateUser(loginDto.getUsername(), loginDto.getPassword());

        log.info("Successfully logged in as {}", userOrSeller.getUsername());

        return JwtAuthenticationResponseDto.builder()
                .timestamp(new Date())
                .username(userOrSeller.getUsername())
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .build();
    }


    private UserDetails getUserDetails(String username) {
        User user = userService.findByUsernameWithoutThrow(username).orElse(null);
        Seller seller = sellerService.findByUsernameWithoutThrow(username).orElse(null);
        return (user == null) ? seller : user;
    }


    private void authenticateUser(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }


    public JwtAuthenticationResponseDto refreshToken(RefreshTokenRequestDto refreshTokenRequest) {
        log.info("Received refresh token request");
        String username;
        try {
            username = jwtParser.extractUsername(refreshTokenRequest.getRefreshToken());
        }
        catch (SignatureException | ExpiredJwtException | MalformedJwtException ex){
            throw new ServiceException(
                    ErrorMessage.TOKEN_IS_INVALID_OR_EXPIRED.getMessage(),
                    ErrorMessage.TOKEN_IS_INVALID_OR_EXPIRED.getStatus()
            );
        }

        User user = userService.findByUsername(username);
        Seller seller = sellerService.findByUsername(username);
        boolean isUser = seller == null;
        return generateResponse(isUser ? user : seller, refreshTokenRequest.getRefreshToken());
    }




    private JwtAuthenticationResponseDto generateResponse(UserDetails userOrSeller, String refreshToken) {
        String jwt = JwtRepository.generateToken(userOrSeller);
        log.info("Updated access token expiration date with refresh token.");

        return JwtAuthenticationResponseDto.builder()
                .timestamp(new Date())
                .username(userOrSeller.getUsername())
                .accessToken(jwt)
                .refreshToken(refreshToken)
                .build();
    }


}
