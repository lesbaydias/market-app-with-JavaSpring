package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserUserId(Long userId);
}
