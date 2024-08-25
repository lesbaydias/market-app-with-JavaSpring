package com.example.marketAppWithJavaSpring.service;

import com.example.marketAppWithJavaSpring.dto.CardInfoResponseDto;
import com.example.marketAppWithJavaSpring.enums.ErrorMessage;
import com.example.marketAppWithJavaSpring.exceptions.ServiceException;
import com.example.marketAppWithJavaSpring.model.Card;
import com.example.marketAppWithJavaSpring.model.User;
import com.example.marketAppWithJavaSpring.repository.CardRepository;
import com.example.marketAppWithJavaSpring.validator.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserService userService;
    private final Time time = new Time();
    public Card getCardByUserId(Long userId){
        return cardRepository.findCardByUserUserId(userId).orElseThrow(()->
                new ServiceException(
                        String.format(ErrorMessage.USERNAME_DOES_NOT_HAVE_SUCH_CARD.getMessage(), userId),
                        ErrorMessage.USERNAME_DOES_NOT_HAVE_SUCH_CARD.getStatus()
                )
        );
    }

    public CardInfoResponseDto getInfoAboutCard(UserDetails userDetails){
        User user = userService.findByUsername(userDetails.getUsername());
        Card card = getCardByUserId(user.getUserId());

        return CardInfoResponseDto.builder()
                .cardId(card.getCardId())
                .cardHoldName(card.getCardHoldName())
                .expirationTime(card.getExpirationTime())
                .cvv(card.getCVV())
                .iban(card.getIBAN())
                .balance(card.getBalance())
                .givenTime(card.getGivenTime())
                .build();
    }


    public void save(User user){
        log.info("Received user with username {} in create card method ", user.getUsername());
        Card card = Card.builder()
                .cardHoldName(user.getFirstname()+" "+user.getLastname())
                .IBAN(generateRandomSixteenDigits())
                .CVV(generateRandomThreeDigits())
                .givenTime(time.dateNow())
                .expirationTime(time.expirationDate())
                .balance(BigDecimal.valueOf(0))
                .user(user)
                .build();
        cardRepository.save(card);
    }
    private int generateRandomThreeDigits() {
        Random random = new Random();
        int randomThreeDigits = random.nextInt(900) + 100;

        while (!checkRandomThreeDigits(randomThreeDigits))
            randomThreeDigits = random.nextInt(900) + 100;

        return randomThreeDigits;
    }
    private boolean checkRandomThreeDigits(Integer cvv){
        Optional<Card> card = cardRepository.findCVV(cvv);
        return card.isEmpty();
    }

    private String generateRandomSixteenDigits() {
        Random random = new Random();
        long randomSixteenDigits = (long)  Math.floor(random.nextDouble() * 9_000_000_000_000_000L) + 1_000_000_000_000_000L;
        while (!checkRandomSixteenDigits(randomSixteenDigits))
            randomSixteenDigits = (long) (Math.floor(random.nextDouble() * 9_000_000_000_000_000L) + 1_000_000_000_000_000L);
        return String.valueOf(randomSixteenDigits);
    }
    private boolean checkRandomSixteenDigits(Long iban){
        Optional<Card> card = cardRepository.findIban(String.valueOf(iban));
        return card.isEmpty();
    }
}
