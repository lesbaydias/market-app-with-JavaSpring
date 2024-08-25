package com.example.marketAppWithJavaSpring.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String productName;
    private String description;
    private BigDecimal price;
    private String selectTypeOfProduct;
    private String category;
    private Long quantityOrWeight;
}
