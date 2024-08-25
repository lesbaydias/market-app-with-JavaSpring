package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.dto.PaymentHistoryResponseDto;
import com.example.marketAppWithJavaSpring.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/show-payment-history")
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentHistoryResponseDto> showPaymentHistory(@AuthenticationPrincipal UserDetails userDetails){
        return paymentService.getPaymentHistory(userDetails);
    }
}
