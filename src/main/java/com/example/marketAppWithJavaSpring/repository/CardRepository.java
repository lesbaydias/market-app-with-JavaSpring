package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("select c from Card c " +
            "where c.IBAN = :iban")
    Optional<Card> findIban(String iban);

    @Query("select c from Card c " +
            "where c.CVV = :cvv")
    Optional<Card> findCVV(Integer cvv);


    @Query("select c from Card c " +
            "where c.user.userId = :userId")
    Optional<Card> findCardByUserUserId(Long userId);
}
