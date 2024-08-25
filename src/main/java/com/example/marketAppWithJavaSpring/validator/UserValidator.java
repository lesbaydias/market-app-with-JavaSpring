package com.example.marketAppWithJavaSpring.validator;

import com.example.marketAppWithJavaSpring.dto.RegisterDto;
import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.service.SellerService;
import com.example.marketAppWithJavaSpring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserService userService;
    private final SellerService sellerService;

    public void validate(RegisterDto signUpRequest) {
        String username = signUpRequest.getUsername();
        String email = signUpRequest.getEmail();
        String phoneNumber = signUpRequest.getPhoneNumber();

        sellerService.findByUsernameWithoutThrow(username)
                .ifPresent(u -> throwUsernameAlreadyExistsException(username));

        userService.findByUsernameWithoutThrow(username)
                .ifPresent(u -> throwUsernameAlreadyExistsException(username));

        userService.findByEmail(email)
                .ifPresent(u -> throwEmailAlreadyExistsException(email));

        userService.findByPhoneNumber(phoneNumber)
                .ifPresent(u -> throwPhoneNumberAlreadyExistsException(phoneNumber));
    }

    private void throwUsernameAlreadyExistsException(String username) {
        throw new ServiceException(
                String.format(ErrorMessage.USERNAME_IS_ALREADY_EXIST.getMessage(), username),
                ErrorMessage.USERNAME_IS_ALREADY_EXIST.getStatus()
        );
    }

    private void throwEmailAlreadyExistsException(String email) {
        throw new ServiceException(
                String.format(ErrorMessage.EMAIL_IS_ALREADY_EXIST.getMessage(), email),
                ErrorMessage.EMAIL_IS_ALREADY_EXIST.getStatus()
        );
    }

    private void throwPhoneNumberAlreadyExistsException(String phoneNumber) {
        throw new ServiceException(
                String.format(ErrorMessage.PHONE_NUMBER_IS_ALREADY_EXIST.getMessage(), phoneNumber),
                ErrorMessage.PHONE_NUMBER_IS_ALREADY_EXIST.getStatus()
        );
    }
}
