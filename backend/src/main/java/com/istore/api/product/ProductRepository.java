package com.istore.api.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findBySlug(String slug);

    Page<Product> findByCategoryAndStatus(String category, String status, Pageable pageable);

    Page<Product> findByStatus(String status, Pageable pageable);
}