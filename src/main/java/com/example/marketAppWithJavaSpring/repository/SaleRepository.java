package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    Optional<Sale> findSaleByProductProductId(Long productId);

    @Transactional
    @Modifying
    @Query("delete from Sale s where s.product.productId = :productId")
    void deleteSaleByProductProductId(Long productId);
}
