package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository  extends JpaRepository<Basket, Long>{
    @Query("select b from Basket b where b.user.userId = :userId")
    Basket findUsersBasket(Long userId);

}
