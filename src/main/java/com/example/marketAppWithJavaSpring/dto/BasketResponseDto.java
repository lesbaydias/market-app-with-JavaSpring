package com.example.marketAppWithJavaSpring.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasketResponseDto {
    private Long basketId;
    private List<BasketProductResponseDto> basketProducts;
    private String username;
}
