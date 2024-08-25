package com.example.marketAppWithJavaSpring.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cards")
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    private String cardHoldName;

    @Column(unique = true)
    private String IBAN;

    private String givenTime;
    private String expirationTime;

    private BigDecimal balance;

    @Column(unique = true)
    private Integer CVV;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;

}

