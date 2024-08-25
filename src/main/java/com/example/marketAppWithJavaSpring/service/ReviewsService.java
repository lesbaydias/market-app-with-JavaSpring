package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.dto.ReviewRequestDto;
import com.example.marketAppWithJavaSpring.dto.ReviewResponseDto;
import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Product;
import com.example.marketAppWithJavaSpring.model.Review;
import com.example.marketAppWithJavaSpring.model.User;
import com.example.marketAppWithJavaSpring.repository.ReviewsRepository;
import com.example.marketAppWithJavaSpring.validator.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final ProductService productService;
    private final UserService userService;
    private final static Time time = new Time();
    public ReviewResponseDto writeReview(ReviewRequestDto reviewRequestDto, UserDetails userDetails){
        Product product = productService.getProductsByProductId(reviewRequestDto.getProductId());
        User user = userService.findByUsername(userDetails.getUsername());

        Review review = Review.builder()
                .rating(reviewRequestDto.getRate())
                .comment(reviewRequestDto.getComment())
                .product(product)
                .reviewTime(time.dateNow())
                .user(user)
                .build();
        reviewsRepository.save(review);

        return mapReviewToDto(review);
    }

    public List<Review> findByProductId(Long productId){
        return reviewsRepository.findReviewByProductProductId(productId);
    }

    public List<ReviewResponseDto> getAllOwnComments(UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        List<Review> reviews = reviewsRepository.findReviewByUserUserId(user.getUserId());
        if(reviews.isEmpty())
            throw new ServiceException(
                    ErrorMessage.YOU_DO_NOT_HAVE_REVIEWS.getMessage(),
                    ErrorMessage.YOU_DO_NOT_HAVE_REVIEWS.getStatus()
            );

        return mapReviewListToDtoList(reviews);
    }

    public List<ReviewResponseDto> getCommentsOfProduct(Long productId){
        List<Review> reviews = reviewsRepository.findReviewByProductProductId(productId);

        if(reviews.isEmpty())
            throw new ServiceException(
                    String.format(ErrorMessage.PRODUCT_DOES_NOT_HAVE_REVIEWS.getMessage(), productId),
                    ErrorMessage.PRODUCT_DOES_NOT_HAVE_REVIEWS.getStatus()
            );
        return mapReviewListToDtoList(reviews);
    }

    @Transactional
    public void deleteReviewByProductId(Long productId){
        reviewsRepository.deleteReviewByProductProductId(productId);
    }

    private ReviewResponseDto mapReviewToDto(Review review) {
        return ReviewResponseDto.builder()
                .reviewId(review.getReviewId())
                .productName(review.getProduct().getProductName())
                .username(review.getUser().getUsername())
                .reviewTime(review.getReviewTime())
                .comment(review.getComment())
                .rating(review.getRating())
                .build();
    }
    private List<ReviewResponseDto> mapReviewListToDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(this::mapReviewToDto)
                .collect(Collectors.toList());
    }
}
