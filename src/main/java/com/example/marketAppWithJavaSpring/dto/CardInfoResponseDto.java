package com.example.marketAppWithJavaSpring.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardInfoResponseDto {
    private Long cardId;
    private String cardHoldName;
    private String givenTime;
    private String expirationTime;
    private BigDecimal balance;
    private String iban;
    private Integer cvv;
}
