package com.example.marketAppWithJavaSpring.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketProductResponseDto {
    private Long basketProductId;
    private String productName;
    private BigDecimal price;
    private String description;
    private String sellerUsername;
    private String sellerPhoneNumber;
    private Long quantity;
}
