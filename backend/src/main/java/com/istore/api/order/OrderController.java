package com.istore.api.order;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping("/checkout")
    public Order checkout(Authentication auth,
                          @RequestParam(defaultValue = "DELIVERY") String fulfilmentMethod,
                          @RequestParam(required = false) String deliveryAddress,
                          @RequestParam(defaultValue = "COD") String paymentMethod) {
        return orderService.checkout(auth.getName(), fulfilmentMethod, deliveryAddress, paymentMethod);
    }

    @GetMapping("/my")
    public Page<Order> myOrders(Authentication auth,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(
                auth.getName(), PageRequest.of(page, size));
    }
// ── ADMIN / STAFF endpoints ──

    @GetMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public Page<Order> allOrders(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size) {
        return orderRepository.findAll(
                PageRequest.of(page, size,
                        org.springframework.data.domain.Sort.by("createdAt").descending()));
    }

    @PatchMapping("/{id}/status")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public Order updateStatus(@PathVariable String id, @RequestParam String status) {
        return orderService.updateStatus(id, status);
    }
}