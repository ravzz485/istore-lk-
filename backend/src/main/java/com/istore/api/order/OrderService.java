package com.istore.api.order;

import com.istore.api.cart.Cart;
import com.istore.api.cart.CartRepository;
import com.istore.api.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final StockService stockService;

    public Order checkout(String customerId, String fulfilmentMethod,
                          String deliveryAddress, String paymentMethod) {

        Cart cart = cartService.getCart(customerId);

        if (cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItem> decremented = new ArrayList<>();   // rollback list

        try {
            for (var cartItem : cart.getItems()) {

                // ⭐ Atomic decrement — fail = out of stock
                boolean ok = stockService.tryDecrementStock(cartItem.getSku(), cartItem.getQty());
                if (!ok) {
                    throw new IllegalArgumentException(
                            "Insufficient stock for: " + cartItem.getProductName()
                                    + " (" + cartItem.getSku() + ")");
                }

                OrderItem item = OrderItem.builder()
                        .sku(cartItem.getSku())
                        .productId(cartItem.getProductId())
                        .productName(cartItem.getProductName())   // snapshot!
                        .qty(cartItem.getQty())
                        .unitPrice(cartItem.getUnitPrice())       // snapshot!
                        .lineTotal(cartItem.getUnitPrice()
                                .multiply(BigDecimal.valueOf(cartItem.getQty())))
                        .build();

                orderItems.add(item);
                decremented.add(item);
            }
        } catch (Exception e) {
            // ⭐ Manual rollback — අඩු කරපු stock ආපහු දෙනවා
            for (OrderItem item : decremented) {
                stockService.restoreStock(item.getSku(), item.getQty());
            }
            throw e;
        }

        BigDecimal total = orderItems.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = orderRepository.save(Order.builder()
                .orderNo(generateOrderNo())
                .customerId(customerId)
                .items(orderItems)
                .fulfilmentMethod(fulfilmentMethod)
                .deliveryAddress(deliveryAddress)
                .paymentMethod(paymentMethod)
                .status("PENDING")
                .total(total)
                .createdAt(Instant.now())
                .build());

        // Cart එක clear
        cart.getItems().clear();
        cart.setUpdatedAt(Instant.now());
        cartRepository.save(cart);

        return order;
    }

    // #IS-2026-00001 format
    private String generateOrderNo() {
        String prefix = "#IS-" + Year.now().getValue() + "-";
        long next = orderRepository.countByOrderNoStartingWith(prefix) + 1;
        return prefix + String.format("%05d", next);

    }
    // Status transition rules — මොන status එකෙන් මොනවට යන්න පුළුවන්ද
    private static final java.util.Map<String, java.util.List<String>> ALLOWED =
            java.util.Map.of(
                    "PENDING",    java.util.List.of("CONFIRMED", "CANCELLED"),
                    "CONFIRMED",  java.util.List.of("PROCESSING", "CANCELLED"),
                    "PROCESSING", java.util.List.of("SHIPPED", "READY_FOR_PICKUP"),
                    "SHIPPED",    java.util.List.of("DELIVERED"),
                    "READY_FOR_PICKUP", java.util.List.of("COLLECTED"),
                    "DELIVERED",  java.util.List.of("COMPLETED"),
                    "COLLECTED",  java.util.List.of("COMPLETED"));

    public Order updateStatus(String orderId, String newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        String current = order.getStatus();

        // 1. Transition එක allowed ද?
        if (!ALLOWED.getOrDefault(current, java.util.List.of()).contains(newStatus)) {
            throw new IllegalArgumentException(
                    "Cannot change status from " + current + " to " + newStatus);
        }

        // 2. ⭐ Cancel වුනොත් — stock ආපහු දෙනවා!
        if ("CANCELLED".equals(newStatus)) {
            for (OrderItem item : order.getItems()) {
                stockService.restoreStock(item.getSku(), item.getQty());
            }
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }
}
