package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.User;
import com.example.marketAppWithJavaSpring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public Optional<User> findByPhoneNumber(String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ServiceException(
                        String.format(ErrorMessage.USERNAME_IS_NOT_FOUND.getMessage(), username),
                        ErrorMessage.USERNAME_IS_NOT_FOUND.getStatus()
                )
        );
    }
    public Optional<User> findByUsernameWithoutThrow(String username){
        return userRepository.findByUsername(username);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }


    @Transactional
    public void deleteUser(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->
                new ServiceException(
                        String.format(ErrorMessage.USERNAME_IS_NOT_FOUND.getMessage(),userId),
                        ErrorMessage.USERNAME_IS_NOT_FOUND.getStatus()
                )
        );
        userRepository.deleteById(user.getUserId());
    }
}
