package com.istore.api.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private String sku;
    private String productId;
    private String productName;   // ⭐ SNAPSHOT — order වෙලාවේ නම
    private int qty;
    private BigDecimal unitPrice; // ⭐ SNAPSHOT — order වෙලාවේ price
    private BigDecimal lineTotal; // unitPrice × qty
}