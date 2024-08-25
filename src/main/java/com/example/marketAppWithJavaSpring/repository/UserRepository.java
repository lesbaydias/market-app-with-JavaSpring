package com.example.marketAppWithJavaSpring.repository;

import com.example.marketAppWithJavaSpring.enums.TypeOfUser;
import com.example.marketAppWithJavaSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    User findUsersByStatus(TypeOfUser typeOfUser);

}

