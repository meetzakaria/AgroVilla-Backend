package com.example.AgroVilla.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_sequence", allocationSize = 1)
    private Long id;

    private String name;
    @Column(length = 1000)
    private String description;
    private Double price;
    private String sku;
    private Integer quantity;
    private String category;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String image; //Base64


}