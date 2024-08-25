package com.example.marketAppWithJavaSpring.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Table(name = "categories")
@Entity
public class Category {
    @Id
    private String categoryTitle;

    @Column(unique = true)
    private String categoryName;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;
}
