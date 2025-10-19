package com.valkoluv.product_catalog_app.controller.rest;

import com.valkoluv.product_catalog_app.model.Product;
import com.valkoluv.product_catalog_app.service.ProductService;
import com.valkoluv.product_catalog_app.service.CategoryService;
import com.valkoluv.product_catalog_app.exception.ResourceNotFoundException;
import com.valkoluv.product_catalog_app.exception.IllegalOperationException;
import com.valkoluv.product_catalog_app.dto.ProductDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductRestController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setPhoto(dto.getPhoto());

        if (dto.getCategoryId() != null) {
            product.setCategory(categoryService.findCategoryById(dto.getCategoryId()));
            if (product.getCategory() == null) {
                throw new ResourceNotFoundException("Category with ID " + dto.getCategoryId() + " not found.");
            }
        }
        return product;
    }

    private ProductDTO convertToDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setPhoto(product.getPhoto());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
        }
        return dto;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String searchName) {

        Page<Product> productPage = productService.findProducts(
                null,
                minPrice,
                maxPrice,
                searchName,
                page,
                size,
                sortBy);

        if (productPage.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Page<ProductDTO> dtoPage = productPage.map(this::convertToDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);

        return product.map(p -> ResponseEntity.ok(convertToDto(p)))
                .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + id + " not found."));
    }


    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        if (productDTO.getId() != null) {
            throw new IllegalOperationException("POST method must be used to create a new resource (ID must be null).");
        }

        Product product = convertToEntity(productDTO);
        Product savedProduct = productService.saveProduct(product);

        return new ResponseEntity<>(convertToDto(savedProduct), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        if (!id.equals(productDTO.getId())) {
            throw new IllegalOperationException("ID in path does not match ID in the request body.");
        }

        if (productDTO.getId() == null || productService.findById(productDTO.getId()).isEmpty()) {
            throw new IllegalOperationException("PUT method must be used to update an existing resource (ID must be present and exist in DB).");
        }

        Product product = convertToEntity(productDTO);
        Product updatedProduct = productService.saveProduct(product);

        return ResponseEntity.ok(convertToDto(updatedProduct));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> illegalPostOperation() {
        throw new IllegalOperationException("POST on an existing resource is not allowed (Use PUT for update).");
    }

    @PutMapping
    public ResponseEntity<Void> illegalPutOperation() {
        throw new IllegalOperationException("PUT without ID is not allowed (Use POST for creation).");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Product with ID " + id + " not found, cannot delete.");
        }

        productService.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}