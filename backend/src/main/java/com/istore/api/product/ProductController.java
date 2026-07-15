package com.istore.api.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    // GET /api/v1/products?category=iPhone&page=0&size=12
    @GetMapping
    public Page<Product> list(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        PageRequest pageable = PageRequest.of(page, size);

        if (category != null && !category.isBlank()) {
            return productRepository.findByCategoryAndStatus(category, "ACTIVE", pageable);
        }
        return productRepository.findByStatus("ACTIVE", pageable);
    }

    // GET /api/v1/products/iphone-16-pro
    @GetMapping("/{slug}")
    public ResponseEntity<Product> getBySlug(@PathVariable String slug) {
        return productRepository.findBySlug(slug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}