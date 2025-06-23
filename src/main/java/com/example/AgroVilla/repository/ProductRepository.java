package com.example.AgroVilla.repository;

import com.example.AgroVilla.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Category অনুযায়ী ফিল্টার
    List<Product> findByCategory(String category);

    // Category ফিল্টারে case-insensitive খোঁজার জন্য
    List<Product> findByCategoryIgnoreCase(String category);

    // Name দিয়ে search (case-insensitive)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByName(@Param("keyword") String keyword);


    Long countByCategoryIgnoreCase(String category);


}