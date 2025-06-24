package com.example.AgroVilla.service;

import com.example.AgroVilla.model.Product;
import com.example.AgroVilla.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepo;

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    private final ProductRepository productRepository;
    private final SKUGeneratorService skuGeneratorService;

    public ProductService(ProductRepository productRepository, SKUGeneratorService skuGeneratorService) {
        this.productRepository = productRepository;
        this.skuGeneratorService = skuGeneratorService;
    }

    public Product addSkuToProduct(Product product) {
        String sku = skuGeneratorService.generateSKU(product.getCategory());
        product.setSku(sku);
        return product;
    }

    public List<Product> searchProducts(String keyword) {
        return productRepo.searchByName(keyword); // ✅ ঠিকভাবে instance call
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setSku(updatedProduct.getSku());
        existing.setQuantity(updatedProduct.getQuantity());
        existing.setCategory(updatedProduct.getCategory());
        if (updatedProduct.getImage() != null) {
            existing.setImage(updatedProduct.getImage());
        }

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }


}
