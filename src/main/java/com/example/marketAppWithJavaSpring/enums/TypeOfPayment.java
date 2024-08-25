package com.example.marketAppWithJavaSpring.enums;

import lombok.*;

@RequiredArgsConstructor
@Getter
public enum TypeOfPayment {
    CREDIT_CARD("Credit card payment"),
    DEBIT_CARD("Debit card payment");
    private final String message;
}
