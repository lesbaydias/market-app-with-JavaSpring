package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.dto.ReviewRequestDto;
import com.example.marketAppWithJavaSpring.dto.ReviewResponseDto;
import com.example.marketAppWithJavaSpring.service.ReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {
    private final ReviewsService reviewsService;

    @PostMapping("/write-comment")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponseDto writeReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ReviewRequestDto reviewRequestDto
    ){
        return reviewsService.writeReview(reviewRequestDto, userDetails);
    }

    @GetMapping("/show-all-own-comments")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> showAllOwnComments(@AuthenticationPrincipal UserDetails userDetails){
        return reviewsService.getAllOwnComments(userDetails);
    }

    @GetMapping("/show-comments-of-product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> showCommentsOfProduct(@RequestParam Long productId){
        return reviewsService.getCommentsOfProduct(productId);
    }
}
