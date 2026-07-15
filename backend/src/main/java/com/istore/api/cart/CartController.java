package com.istore.api.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Cart getCart(Authentication auth) {
        return cartService.getCart(auth.getName());   // ⭐ token එකේ userId!
    }

    @PostMapping("/items")
    public Cart addItem(Authentication auth,
                        @RequestParam String sku,
                        @RequestParam(defaultValue = "1") int qty) {
        return cartService.addItem(auth.getName(), sku, qty);
    }

    @DeleteMapping("/items/{sku}")
    public Cart removeItem(Authentication auth, @PathVariable String sku) {
        return cartService.removeItem(auth.getName(), sku);
    }
}