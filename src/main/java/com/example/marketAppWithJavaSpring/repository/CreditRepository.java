package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findCreditByCardUserUserId(Long userId);
}
