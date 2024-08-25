package com.example.marketAppWithJavaSpring.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private String product_name;
    private String description;
    private String type_of_product;
    private String category;
    private String image;
    private BigDecimal price;
    private String seller_name;
    private String seller_username;
    private String seller_phone_number;
}
