package com.example.marketAppWithJavaSpring.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String orderTime;
    private BigDecimal totalAmount;
    private String username;
    private List<ProductResponseDto> products;
}
