package com.example.marketAppWithJavaSpring.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponseDto {
    private Long reviewId;
    private Integer rating;
    private String comment;
    private String reviewTime;
    private String username;
    private String productName;
}
