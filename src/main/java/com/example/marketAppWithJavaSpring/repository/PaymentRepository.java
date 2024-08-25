package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p " +
            "where p.orders.user.userId = :userId")
    List<Payment> findPaymentByUserId(Long userId);
}
