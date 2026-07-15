package com.istore.api.cart;

import com.istore.api.product.Product;
import com.istore.api.product.ProductRepository;
import com.istore.api.product.Variant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    // User ගේ cart එක ගන්නවා — නැත්නම් අලුත් හිස් එකක් හදනවා
    public Cart getCart(String customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .customerId(customerId)
                                .updatedAt(Instant.now())
                                .build()));
    }

    public Cart addItem(String customerId, String sku, int qty) {

        // 1. SKU එක ඇත්තටම තියෙනවද? (fake SKU block!)
        Product product = productRepository.findAll().stream()
                .filter(p -> p.getVariants().stream()
                        .anyMatch(v -> v.getSku().equals(sku)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + sku));

        Variant variant = product.getVariants().stream()
                .filter(v -> v.getSku().equals(sku))
                .findFirst().orElseThrow();

        // 2. Stock එක check
        if (variant.getStock() < qty) {
            throw new IllegalArgumentException("Only " + variant.getStock() + " left in stock");
        }

        Cart cart = getCart(customerId);

        // 3. Same SKU already cart එකේ නම් — qty එක update; නැත්නම් අලුත් line එකක්
        cart.getItems().stream()
                .filter(i -> i.getSku().equals(sku))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQty(item.getQty() + qty),
                        () -> cart.getItems().add(CartItem.builder()
                                .sku(sku)
                                .productId(product.getId())
                                .productName(product.getName())
                                .qty(qty)
                                .unitPrice(variant.getPrice())
                                .build()));

        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }

    public Cart removeItem(String customerId, String sku) {
        Cart cart = getCart(customerId);
        cart.getItems().removeIf(i -> i.getSku().equals(sku));
        cart.setUpdatedAt(Instant.now());
        return cartRepository.save(cart);
    }
}