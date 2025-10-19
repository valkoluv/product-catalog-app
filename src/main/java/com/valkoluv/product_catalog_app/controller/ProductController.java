package com.valkoluv.product_catalog_app.controller;

import com.valkoluv.product_catalog_app.model.Product;
import com.valkoluv.product_catalog_app.service.ProductService;
import com.valkoluv.product_catalog_app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String searchName) {

        Page<Product> productPage = productService.findProducts(
                categoryId, minPrice, maxPrice, searchName, page, size, sortBy);

        model.addAttribute("categories", categoryService.findAllCategories());

        model.addAttribute("productPage", productPage);

        model.addAttribute("currentPage", page);
        model.addAttribute("currentSortBy", sortBy);
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("currentMinPrice", minPrice);
        model.addAttribute("currentMaxPrice", maxPrice);
        model.addAttribute("currentSearchName", searchName);

        return "products";
    }
}