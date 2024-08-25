package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.service.SellerService;
import com.example.marketAppWithJavaSpring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final SellerService sellerService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> seeAllUsers(){
        return userService.findAll().stream()
                .filter(user -> !"ADMIN".equals(user.getStatus().name()))
                .map(user -> Map.of(
                        "id", String.valueOf(user.getUserId()),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/sellers")
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> seeAllSellers(){
        return sellerService.findAll().stream()
                .map(seller -> Map.of(
                        "id", String.valueOf(seller.getSellerId()),
                        "name",seller.getSellerName(),
                        "phone", seller.getPhoneNumber(),
                        "role", seller.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/sellers/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteSeller(@RequestParam Long sellerId){
        sellerService.deleteSeller(sellerId);
        return ResponseEntity.ok("Seller with id="+sellerId+" is successfully deleted");
    }
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteUser(@RequestParam Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok("User with id="+userId+" is successfully deleted");
    }
}
