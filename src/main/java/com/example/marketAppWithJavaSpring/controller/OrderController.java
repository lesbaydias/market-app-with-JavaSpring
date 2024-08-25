package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.dto.OrderResponseDto;
import com.example.marketAppWithJavaSpring.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/addOrder")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto addOrder(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String selectTypeOfPayment){
        return orderService.addOrder(userDetails, selectTypeOfPayment);
    }

    @GetMapping("/show-all-orders")
    @ResponseStatus(HttpStatus.OK)
    private List<OrderResponseDto> showAllOrders(@AuthenticationPrincipal UserDetails userDetails){
        return orderService.getAllOrders(userDetails);
    }
}
