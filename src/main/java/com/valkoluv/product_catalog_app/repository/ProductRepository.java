package com.valkoluv.product_catalog_app.repository;

import com.valkoluv.product_catalog_app.model.Category;
import com.valkoluv.product_catalog_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCaseAndPriceBetweenAndCategory(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Category category,
            Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndPriceBetween(
            String name,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable);
}
