package com.example.marketAppWithJavaSpring.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "basket_product")
public class BasketProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long basketProductId;


    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "product_id")
    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    @Column(name = "quantity")
    private Long quantity;
}
