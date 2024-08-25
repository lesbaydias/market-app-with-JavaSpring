package com.example.marketAppWithJavaSpring.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {
    USERNAME_IS_ALREADY_EXIST("Username is already exits: %s .", HttpStatus.BAD_REQUEST),
    EMAIL_IS_ALREADY_EXIST("Email is already exits: %s .", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_IS_ALREADY_EXIST("Phone number is already exits: %s .", HttpStatus.BAD_REQUEST),
    ROLE_IS_NOT_FOUND("Role is not found: %s .", HttpStatus.NOT_FOUND),
    USERNAME_IS_NOT_FOUND("Username is not found: %s .", HttpStatus.NOT_FOUND),
    PASSWORD_IS_WRONG("Password is incorrect: %s .", HttpStatus.BAD_REQUEST),
    TOKEN_IS_INVALID_OR_EXPIRED("Token is invalid or expired.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_IS_EXPIRED("Refresh token is expired.", HttpStatus.UNAUTHORIZED),
    CATEGORY_IS_NOT_FOUND("Category is not found: %s .", HttpStatus.NOT_FOUND),
    SELLER_IS_NOT_FOUND("Seller is not found: %s .", HttpStatus.NOT_FOUND),
    PRODUCT_IS_NOT_FOUND("Product is not found: %s .", HttpStatus.NOT_FOUND),
    PRODUCT_NAME_IS_ALREADY_EXIST("Product name is already exist: %s .", HttpStatus.BAD_REQUEST),
    USERNAME_DOES_NOT_HAVE_SUCH_CARD("The user does not have such a card: id=%s .", HttpStatus.NOT_FOUND),
    ORDER_IS_EMPTY("Order is empty.", HttpStatus.OK),
    YOU_DO_NOT_HAVE_REVIEWS("You don't have any reviews.", HttpStatus.OK),
    PRODUCT_DOES_NOT_HAVE_REVIEWS("Product with id=%s doesn't have any reviews.", HttpStatus.OK),
    LIST_IS_EMPTY("List is empty.", HttpStatus.OK),
    SALES_LIST_IS_EMPTY("We don't have any sales list.", HttpStatus.OK),
    CREDIT_LIST_IS_EMPTY("You don't have any credits.", HttpStatus.OK),
    IMAGE_MUST_HAVE_NAME("Image must have name", HttpStatus.BAD_REQUEST),
    PRODUCT_IS_NOT_AVAILABLE("Product with id=%s is not available.", HttpStatus.BAD_REQUEST),
    UNAVAILABLE_OPERATION("Unavailable operation. You don't have products", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_MONEY("You don't have enough money. Total amount=%s$. You have only %s$.", HttpStatus.BAD_REQUEST),
    PAYMENT_TYPE_IS_NOT_FOUND("Payment type is not found: %s", HttpStatus.NOT_FOUND)

    ;

    private final String message;
    private final HttpStatus status;

    ErrorMessage(String errorMessage, HttpStatus status){
        this.message = errorMessage;
        this.status = status;
    }

}
