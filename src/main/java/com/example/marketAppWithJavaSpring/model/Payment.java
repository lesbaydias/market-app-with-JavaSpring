package com.example.marketAppWithJavaSpring.model;

import com.example.marketAppWithJavaSpring.enums.TypeOfPayment;
import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String paymentTime;
    private BigDecimal paymentAmount;

    @Column(name = "typeOfPayment")
    @Enumerated(EnumType.STRING)
    private TypeOfPayment typeOfPayment;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "orderId")
    private Order orders;


}
