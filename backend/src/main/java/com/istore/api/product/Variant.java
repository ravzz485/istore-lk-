package com.istore.api.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Variant {

    private String sku;              // උදා: IP16P-256-NAT (unique!)
    private String colorName;        // Natural Titanium
    private String colorHex;         // #c2bcb2 — UI එකේ color dot එකට
    private String storage;          // 256GB
    private String condition;        // BRAND_NEW / REFURBISHED / PRE_OWNED
    private BigDecimal price;        // LKR — BigDecimal, float නෙමෙයි!
    private int stock;
    private int lowStockThreshold;
    private List<String> images;
}