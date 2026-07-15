package com.istore.api.config;

import com.istore.api.product.Product;
import com.istore.api.product.ProductRepository;
import com.istore.api.product.Variant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {

        // දැනටමත් products තියෙනවා නම් — ආයෙත් දාන්නේ නෑ
        if (productRepository.count() > 0) return;

        Product iphone = Product.builder()
                .name("iPhone 16 Pro")
                .slug("iphone-16-pro")
                .category("iPhone")
                .modelNumber("A3101")
                .releaseYear(2024)
                .description("The ultimate iPhone with A18 Pro chip and titanium design.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "A18 Pro",
                        "display", "6.3-inch Super Retina XDR",
                        "camera", "48MP Fusion | 48MP Ultra Wide | 12MP Telephoto",
                        "battery", "Up to 27 hours video playback"))
                .variants(List.of(
                        Variant.builder().sku("IP16P-256-NAT")
                                .colorName("Natural Titanium").colorHex("#c2bcb2")
                                .storage("256GB").condition("BRAND_NEW")
                                .price(new BigDecimal("434900.00"))
                                .stock(12).lowStockThreshold(3)
                                .images(List.of()).build(),
                        Variant.builder().sku("IP16P-512-BLU")
                                .colorName("Blue Titanium").colorHex("#3f4a5a")
                                .storage("512GB").condition("BRAND_NEW")
                                .price(new BigDecimal("512900.00"))
                                .stock(4).lowStockThreshold(3)
                                .images(List.of()).build()))
                .createdAt(Instant.now())
                .build();

        Product macbook = Product.builder()
                .name("MacBook Air M3")
                .slug("macbook-air-m3")
                .category("Mac")
                .modelNumber("A3113")
                .releaseYear(2024)
                .description("Strikingly thin and fast, with the M3 chip.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "Apple M3",
                        "display", "13.6-inch Liquid Retina",
                        "ram", "8GB unified memory",
                        "battery", "Up to 18 hours"))
                .variants(List.of(
                        Variant.builder().sku("MBA-M3-256-MID")
                                .colorName("Midnight").colorHex("#2e3641")
                                .storage("256GB SSD").condition("BRAND_NEW")
                                .price(new BigDecimal("389900.00"))
                                .stock(7).lowStockThreshold(2)
                                .images(List.of()).build()))
                .createdAt(Instant.now())
                .build();

        Product airpods = Product.builder()
                .name("AirPods Pro 2")
                .slug("airpods-pro-2")
                .category("AirPods")
                .modelNumber("A3048")
                .releaseYear(2023)
                .description("Adaptive Audio. Now with USB-C.")
                .status("ACTIVE")
                .specs(Map.of(
                        "anc", "Active Noise Cancellation",
                        "battery", "Up to 6 hours listening",
                        "connector", "USB-C"))
                .variants(List.of(
                        Variant.builder().sku("APP2-USBC-WHT")
                                .colorName("White").colorHex("#ffffff")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("84900.00"))
                                .stock(20).lowStockThreshold(5)
                                .images(List.of()).build()))
                .createdAt(Instant.now())
                .build();

        productRepository.saveAll(List.of(iphone, macbook, airpods));
        System.out.println("✅ Seeded " + productRepository.count() + " products!");
    }
}