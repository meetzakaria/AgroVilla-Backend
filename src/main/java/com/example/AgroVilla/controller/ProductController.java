package com.example.AgroVilla.controller;

import com.example.AgroVilla.dto.ProductForm;
import com.example.AgroVilla.model.Product;
import com.example.AgroVilla.repository.ProductRepository;
import com.example.AgroVilla.repository.UserRepository;
import com.example.AgroVilla.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    // CREATE Product
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(@ModelAttribute ProductForm form) {
        try {
            Product product = new Product();
            product.setName(form.getName());
            product.setDescription(form.getDescription());
            product.setPrice(form.getPrice());
            product.setQuantity(form.getQuantity());
            product.setCategory(form.getCategory());

            MultipartFile image = form.getImage();
            if (image != null && !image.isEmpty()) {
                // Base64 encode the image
                byte[] imageBytes = image.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                product.setImage(base64Image); // Assuming your entity field is String for Base64
            } else {
                return ResponseEntity.badRequest().body("Image is required");
            }

            // Generate SKU
            productService.addSkuToProduct(product);

            Product saved = productRepository.save(product);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        return ResponseEntity.ok(products);
    }



    // READ all products
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // READ product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found"));
    }


    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @ModelAttribute ProductForm form) {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (!optionalProduct.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Product not found with ID: " + id);
            }

            Product product = optionalProduct.get();
            product.setName(form.getName());
            product.setDescription(form.getDescription());
            product.setPrice(form.getPrice());
            product.setQuantity(form.getQuantity());
            product.setCategory(form.getCategory());

            MultipartFile image = form.getImage();
            if (image != null && !image.isEmpty()) {
                byte[] imageBytes = image.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                product.setImage(base64Image);
            }

            // Optionally regenerate SKU if necessary
            productService.addSkuToProduct(product);

            Product updated = productRepository.save(product);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error occurred while updating: " + e.getMessage());
        }
    }


    // DELETE Product
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok("✅ Product deleted successfully");
    }
}











//package com.example.AgroVilla.controller;
//
//import com.example.AgroVilla.dto.ProductForm;
//import com.example.AgroVilla.model.Product;
//import com.example.AgroVilla.model.User;
//import com.example.AgroVilla.repository.ProductRepository;
//import com.example.AgroVilla.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private ProductService productService;
//
//
//    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> addProduct(@ModelAttribute ProductForm form) {
//        try {
//            System.out.println("📦 Received Product: " + form.getName());
//            System.out.println("✅ Image received: " + form.getImage().getOriginalFilename());
//
//            Product product = new Product();
//            product.setName(form.getName());
//            product.setDescription(form.getDescription());
//            product.setPrice(form.getPrice());
//            product.setQuantity(form.getQuantity());
//            product.setCategory(form.getCategory());
//
//            // ✅ Handle image correctly
//            if (form.getImage() != null && !form.getImage().isEmpty()) {
//                product.setImage(form.getImage().getBytes());
//            } else {
//                return ResponseEntity.badRequest().body("❌ Image is required");
//            }
//
//
//            // ✅ Generate SKU
//            productService.addSkuToProduct(product);
//
//            // ✅ Save product
//            Product saved = productRepository.save(product);
//            return ResponseEntity.ok(saved);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("❌ Error occurred: " + e.getMessage());
//        }
//    }
//
//
//
////    @PostMapping(value = "/api/products/add", consumes = MediaType.APPLICATION_JSON_VALUE)
////    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
////        // প্রোডাক্ট সেভ করুন
////        return ResponseEntity.ok(product);
////    }
////
////
////    public ResponseEntity<Product> addProduct(
////            @RequestParam("name") String name,
////            @RequestParam("description") String description,
////            @RequestParam("price") Double price,
////            @RequestParam("quantity") Integer quantity,
////            @RequestParam("category") String category,
////            @RequestParam("image") MultipartFile imageFile
////    ) {
////        try {
////            Product product = new Product();
////            product.setName(name);
////            product.setDescription(description);
////            product.setPrice(price);
////            product.setQuantity(quantity);
////            product.setCategory(category);
////            product.setImage(imageFile.getBytes());
////
////            Product saved = productRepository.save(product);
////            return ResponseEntity.ok(saved);
////        } catch (Exception e) {
////            return ResponseEntity.badRequest().build();
////        }
////    }
//
//
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadFile(
//            @RequestPart("file") MultipartFile file,
//            @RequestPart("description") String description) {
//        // ফাইল প্রসেসিং
//        return ResponseEntity.ok("File uploaded successfully!");
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
//        return ResponseEntity.ok(productRepository.findAll());
//    }
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
//        return ResponseEntity.ok(productRepository.findByCategoryIgnoreCase(category));
//    }
//
//    @GetMapping("/search")
//    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
//        return ResponseEntity.ok(productService.searchProducts(keyword));
//    }
//
//    @PostMapping
//    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
//        Product savedProduct = productService.addSkuToProduct(product); // ✅ service call
//        return ResponseEntity.ok(savedProduct);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Product> updateProduct(
//            @PathVariable Long id,
//            @RequestBody Product product
//    ) {
//        try {
//            Product updated = productService.updateProduct(id, product);
//            return ResponseEntity.ok(updated);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
//        try {
//            productService.deleteProduct(id);
//            return ResponseEntity.noContent().build(); // 204
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
//}
//
//
////
////
////    @GetMapping
////    public ResponseEntity<List<Product>> getAll() {
////        return ResponseEntity.ok(productRepository.findAll());
////    }
////
////    @GetMapping("/category/{category}")
////    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {
////        return ResponseEntity.ok(productRepository.findByCategory(category));
////    }
////
////    @GetMapping("/products/category/{category}")
////    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
////        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
////        return ResponseEntity.ok(products);
////    }
