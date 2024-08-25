package com.example.marketAppWithJavaSpring.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditResponseDto {
    private Long creditId;

    private String iban;

    private String startTime;
    private String endTime;

    private BigDecimal monthlyPayment;
}
