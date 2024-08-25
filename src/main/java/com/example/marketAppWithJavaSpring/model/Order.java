package com.example.marketAppWithJavaSpring.model;

import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String orderTime;
    private BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "orderProducts",
        joinColumns = @JoinColumn(name = "orderId"),
        inverseJoinColumns = @JoinColumn(name = "productId")
    )
    private List<Product> products;

}
