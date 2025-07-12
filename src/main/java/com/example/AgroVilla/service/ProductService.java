package com.example.AgroVilla.service;

import com.example.AgroVilla.model.Product;
import com.example.AgroVilla.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SKUGeneratorService skuGeneratorService;

    @Autowired
    public ProductService(ProductRepository productRepository, SKUGeneratorService skuGeneratorService) {
        this.productRepository = productRepository;
        this.skuGeneratorService = skuGeneratorService;
    }

    // ✅ Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ Add a new product with auto-generated SKU
    public Product addProduct(Product product) {
        String sku = skuGeneratorService.generateSKU(product.getCategory());
        product.setSku(sku);
        return productRepository.save(product);
    }

    // ✅ Search products by name
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByName(keyword);
    }

    // ✅ Update an existing product
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setQuantity(updatedProduct.getQuantity());
        existing.setCategory(updatedProduct.getCategory());

        // যদি নতুন image দেওয়া হয়, তাহলে সেটাও সেট করো
        if (updatedProduct.getImage() != null && !updatedProduct.getImage().isEmpty()) {
            existing.setImage(updatedProduct.getImage());
        }

        // SKU manually update করা হচ্ছে এখানে, চাইলে এ অংশ skip করেও শুধু skuGenerator দিয়ে auto set করতে পারো
        if (updatedProduct.getSku() != null && !updatedProduct.getSku().isEmpty()) {
            existing.setSku(updatedProduct.getSku());
        }

        return productRepository.save(existing);
    }

    // ✅ Delete product by ID
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public void addSkuToProduct(Product product) {
    }
}
