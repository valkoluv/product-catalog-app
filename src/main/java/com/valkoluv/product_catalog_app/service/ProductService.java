package com.valkoluv.product_catalog_app.service;

import com.valkoluv.product_catalog_app.model.Product;
import com.valkoluv.product_catalog_app.model.Category;
import com.valkoluv.product_catalog_app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Page<Product> findProducts(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String searchName,
            int page,
            int size,
            String sortBy) {

        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String effectiveSearchName = searchName != null ? searchName : "";
        BigDecimal effectiveMinPrice = minPrice != null ? minPrice : BigDecimal.ZERO;
        BigDecimal effectiveMaxPrice = maxPrice != null ? maxPrice : new BigDecimal("999999");

        if (categoryId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            if (category != null) {
                return productRepository.findByNameContainingIgnoreCaseAndPriceBetweenAndCategory(
                        effectiveSearchName,
                        effectiveMinPrice,
                        effectiveMaxPrice,
                        category,
                        pageable);
            }
        }

        return productRepository.findByNameContainingIgnoreCaseAndPriceBetween(
                effectiveSearchName,
                effectiveMinPrice,
                effectiveMaxPrice,
                pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
