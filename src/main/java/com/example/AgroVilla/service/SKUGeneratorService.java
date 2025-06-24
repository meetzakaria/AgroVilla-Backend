package com.example.AgroVilla.service;

import com.example.AgroVilla.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class SKUGeneratorService {

    private final ProductRepository productRepository;

    public SKUGeneratorService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String generateSKU(String category) {
        System.out.println("✅ SKUGenerator Called with category: " + category); // Fertilizers, fertilizers

        String prefix = switch (category) {
            case "Seeds" -> "SEED";
            case "Fertilizers" -> "FERT";
            case "Equipment" -> "EQPM";
            case "Machinery" -> "MACH";
            case "Saplings" -> "SAPL";
            default -> "GEN";
        };

        Long count = productRepository.countByCategoryIgnoreCase(category);
        System.out.println("Count found: " + count);

        String sku = prefix + "-" + String.format("%03d", count + 1);
        System.out.println("Final SKU: " + sku);

        return sku;
    }
}
