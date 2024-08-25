package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.dto.BasketResponseDto;
import com.example.marketAppWithJavaSpring.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final BasketService basketService;
    @GetMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public UserDetails profile(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }
    @PostMapping("/product/toBasket/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BasketResponseDto toBasket(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long productId){
        return basketService.addToBasketProduct(userDetails, productId);
    }
    @DeleteMapping("/product/clean-basket")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteProductFromBasket(@AuthenticationPrincipal UserDetails userDetails){
        basketService.deleteProductFromBasket(userDetails);
        return ResponseEntity.ok("Successfully cleaned basket");
    }

    @DeleteMapping("/product/quantity/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteProductQuantityFromBasket(@AuthenticationPrincipal UserDetails userDetails,@RequestParam Long productId){
        basketService.deleteProductQuantityFromBasket(userDetails, productId);
        return ResponseEntity.ok("Successfully deleted product id with={"+productId+"} by 1 quantity");
    }
}
