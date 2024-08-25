package com.example.marketAppWithJavaSpring.model;

import com.example.marketAppWithJavaSpring.enums.TypeOfProduct;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private String description;
    private BigDecimal price;

    @Column(name = "image")
    private String image;

    @Column(name = "typeOfProduct")
    @Enumerated(EnumType.STRING)
    private TypeOfProduct typeOfProduct;

    @Column(name = "quantityOrWeight")
    private Long quantityOrWeight;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sellerId")
    private Seller seller;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_title")
    private Category category;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;
}

