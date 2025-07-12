package com.example.AgroVilla.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ProductForm {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String category;
    private MultipartFile image;

}