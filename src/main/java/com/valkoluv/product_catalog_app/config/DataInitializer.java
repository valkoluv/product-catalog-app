package com.valkoluv.product_catalog_app.config;

import com.valkoluv.product_catalog_app.model.Category;
import com.valkoluv.product_catalog_app.model.Product;
import com.valkoluv.product_catalog_app.repository.CategoryRepository;
import com.valkoluv.product_catalog_app.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            if (categoryRepository.count() == 0 && productRepository.count() == 0) {

                Category electronics = new Category("Electronics");
                Category books = new Category("Books");
                Category clothing = new Category("Clothing");

                categoryRepository.saveAll(Arrays.asList(electronics, books, clothing));

                Product phone = new Product("Smartphone", BigDecimal.valueOf(699.99),
                        "https://example.com/images/phone.jpg", electronics);
                Product laptop = new Product("Laptop", BigDecimal.valueOf(1199.99),
                        "https://example.com/images/laptop.jpg", electronics);
                Product novel = new Product("The Great Gatsby", BigDecimal.valueOf(15.99),
                        "https://example.com/images/gatsby.jpg", books);
                Product tshirt = new Product("T-shirt", BigDecimal.valueOf(19.99),
                        "https://example.com/images/tshirt.jpg", clothing);

                productRepository.saveAll(Arrays.asList(phone, laptop, novel, tshirt));

                System.out.println("✅ Sample data initialized successfully!");
            } else {
                System.out.println("ℹ️ Data already exists. Skipping initialization.");
            }
        };
    }
}
