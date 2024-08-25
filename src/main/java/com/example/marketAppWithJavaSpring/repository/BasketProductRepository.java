package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.BasketProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface BasketProductRepository extends JpaRepository<BasketProduct, Long> {
    @Transactional
    @Modifying
    @Query("delete from BasketProduct bp where bp.product.productId = :productId")
    void deleteBasketProductByProductProductId(Long productId);
}
