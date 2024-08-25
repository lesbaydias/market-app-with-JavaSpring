package com.example.marketAppWithJavaSpring.controller;

import com.example.marketAppWithJavaSpring.dto.CardInfoResponseDto;
import com.example.marketAppWithJavaSpring.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    @GetMapping("/show-info-about-card")
    @ResponseStatus(HttpStatus.OK)
    public CardInfoResponseDto showInfoAboutCard(@AuthenticationPrincipal UserDetails userDetails){
        return cardService.getInfoAboutCard(userDetails);
    }

}
