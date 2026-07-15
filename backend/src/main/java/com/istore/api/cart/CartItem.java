package com.istore.api.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private String sku;           // මොන variant එකද (IP16P-256-NAT)
    private String productId;
    private String productName;   // display එකට
    private int qty;
    private BigDecimal unitPrice; // add කරද්දී තිබ්බ price එක
}