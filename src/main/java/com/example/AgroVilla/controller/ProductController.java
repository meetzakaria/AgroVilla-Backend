package com.example.AgroVilla.controller;

import com.example.AgroVilla.dto.ProductForm;
import com.example.AgroVilla.model.Product;
import com.example.AgroVilla.repository.ProductRepository;
import com.example.AgroVilla.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(@ModelAttribute ProductForm form) {
        try {
            Product product = new Product();
            product.setName(form.getName());
            product.setDescription(form.getDescription());
            product.setPrice(form.getPrice());
            product.setQuantity(form.getQuantity());
            product.setCategory(form.getCategory());
            product.setImage(form.getImage().getBytes());

            // Add sku to product
            productService.addSkuToProduct(product);

            Product saved = productRepository.save(product);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    @PostMapping(value = "/api/products/add", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
//        // প্রোডাক্ট সেভ করুন
//        return ResponseEntity.ok(product);
//    }
//
//
//    public ResponseEntity<Product> addProduct(
//            @RequestParam("name") String name,
//            @RequestParam("description") String description,
//            @RequestParam("price") Double price,
//            @RequestParam("quantity") Integer quantity,
//            @RequestParam("category") String category,
//            @RequestParam("image") MultipartFile imageFile
//    ) {
//        try {
//            Product product = new Product();
//            product.setName(name);
//            product.setDescription(description);
//            product.setPrice(price);
//            product.setQuantity(quantity);
//            product.setCategory(category);
//            product.setImage(imageFile.getBytes());
//
//            Product saved = productRepository.save(product);
//            return ResponseEntity.ok(saved);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("description") String description) {
        // ফাইল প্রসেসিং
        return ResponseEntity.ok("File uploaded successfully!");
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productRepository.findByCategoryIgnoreCase(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.addSkuToProduct(product); // ✅ service call
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product
    ) {
        try {
            Product updated = productService.updateProduct(id, product);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build(); // 204
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


//
//
//    @GetMapping
//    public ResponseEntity<List<Product>> getAll() {
//        return ResponseEntity.ok(productRepository.findAll());
//    }
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
//        return ResponseEntity.ok(productRepository.findByCategory(category));
//    }
//
//    @GetMapping("/products/category/{category}")
//    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
//        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
//        return ResponseEntity.ok(products);
//    }
