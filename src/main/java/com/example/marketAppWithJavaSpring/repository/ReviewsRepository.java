package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Review, Long> {

    List<Review> findReviewByUserUserId(Long userId);
    List<Review> findReviewByProductProductId(Long productId);

    @Modifying
    @Transactional
    @Query("delete from Review r where r.product.productId = :productId")
    void deleteReviewByProductProductId(Long productId);
}
