package com.example.marketAppWithJavaSpring.dto;

import com.example.marketAppWithJavaSpring.model.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistoryResponseDto {
    private Long paymentId;
    private String paymentTime;
    private BigDecimal paymentAmount;
    private String typeOfPayment;
    private Long orderId;
    private String orderTime;
    private String username;
    private List<Product> products;
}
