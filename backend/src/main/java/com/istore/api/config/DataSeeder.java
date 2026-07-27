package com.istore.api.config;

import com.istore.api.product.Product;
import com.istore.api.product.ProductRepository;
import com.istore.api.product.Variant;
import com.istore.api.user.Role;
import com.istore.api.user.User;
import com.istore.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (!userRepository.existsByEmail("admin@istore.lk")) {
            userRepository.save(User.builder()
                    .fullName("iStore Admin")
                    .email("admin@istore.lk")
                    .password(passwordEncoder.encode("Admin@123"))
                    .phone("0770000000")
                    .nic("000000000000")
                    .role(Role.ADMIN)
                    .suspended(false)
                    .createdAt(Instant.now())
                    .build());
            System.out.println("✅ Seeded admin user: admin@istore.lk / Admin@123");
        }

        // ⭐ Guard එක ආපහු දාලා. deleteAll() production එකේ තිබ්බොත් හැම restart
        //    එකකදිම database එක මැකෙනවා — orders/users ඔක්කොම නැති වෙනවා!
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
                                .images(List.of("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-16-pro-finish-select-202409-6-3inch-naturaltitanium?wid=800&hei=800&fmt=png-alpha"))
                                .build(),
                        Variant.builder().sku("IP16P-512-BLU")
                                .colorName("Blue Titanium").colorHex("#3f4a5a")
                                .storage("512GB").condition("BRAND_NEW")
                                .price(new BigDecimal("512900.00"))
                                .stock(4).lowStockThreshold(3)
                                .images(List.of("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-16-pro-finish-select-202409-6-3inch-bluetitanium?wid=800&hei=800&fmt=png-alpha"))
                                .build()))
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
                                .images(List.of("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/macbook-air-13-midnight-select-202402?wid=800&hei=800&fmt=png-alpha"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        // ── ⭐ MacBook Pro — නව product! ──
        Product macbookPro = Product.builder()
                .name("MacBook Pro")
                .slug("macbook-pro")
                .category("Mac")
                .modelNumber("A2991")
                .releaseYear(2025)
                .description("Mind-blowing. Head-turning. Supercharged by the M4 Pro chip for demanding workflows.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "Apple M4 Pro",
                        "display", "14-inch Liquid Retina XDR",
                        "ram", "18GB unified memory",
                        "battery", "Up to 22 hours"))
                .variants(List.of(
                        Variant.builder().sku("MBP-M4-512-SPBLK")
                                .colorName("Space Black").colorHex("#3b3b3d")
                                .storage("512GB SSD").condition("BRAND_NEW")
                                .price(new BigDecimal("389000.00"))
                                .stock(9).lowStockThreshold(3)
                                .images(List.of("/pro.png"))
                                .build()))
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
                                .images(List.of("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/airpods-pro-2-hero-select-202409?wid=800&hei=800&fmt=png-alpha"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product airpodsMax = Product.builder()
                .name("AirPods Max")
                .slug("airpods-max")
                .category("AirPods")
                .modelNumber("A2096")
                .releaseYear(2024)
                .description("An unparalleled listening experience with high-fidelity audio and computational audio.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "Apple H1 (dual chip)",
                        "anc", "Active Noise Cancellation with Transparency mode",
                        "battery", "Up to 20 hours listening time",
                        "audio", "Custom acoustic design with 40mm dynamic driver"))
                .variants(List.of(
                        Variant.builder().sku("APM-MIDNIGHT")
                                .colorName("Midnight").colorHex("#2e3641")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("164900.00"))
                                .stock(10).lowStockThreshold(3)
                                .images(List.of("/airpods_max_midnight__ddy8oa1y3y4i_large_2x.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        // ── ⭐ Apple Watch products ──
        Product watchSeries11 = Product.builder()
                .name("Apple Watch Series 11")
                .slug("apple-watch-series-11")
                .category("Watch")
                .modelNumber("A2986")
                .releaseYear(2025)
                .description("The most advanced Apple Watch, with a larger display and powerful health features.")
                .status("ACTIVE")
                .specs(Map.of(
                        "display", "Always-On Retina LTPO OLED",
                        "chip", "S10 SiP",
                        "health", "ECG, Blood Oxygen, Temperature sensing",
                        "battery", "Up to 18 hours"))
                .variants(List.of(
                        Variant.builder().sku("AWS11-45-MID")
                                .colorName("Midnight").colorHex("#2e3641")
                                .storage("45mm GPS").condition("BRAND_NEW")
                                .price(new BigDecimal("139900.00"))
                                .stock(15).lowStockThreshold(4)
                                .images(List.of("/watch-series11.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product watchSE3 = Product.builder()
                .name("Apple Watch SE 3")
                .slug("apple-watch-se-3")
                .category("Watch")
                .modelNumber("A2723")
                .releaseYear(2025)
                .description("All the essentials at a great value, with powerful safety features.")
                .status("ACTIVE")
                .specs(Map.of(
                        "display", "Retina LTPO OLED",
                        "chip", "S10 SiP",
                        "safety", "Crash Detection, Fall Detection, Emergency SOS",
                        "battery", "Up to 18 hours"))
                .variants(List.of(
                        Variant.builder().sku("AWSE3-44-STAR")
                                .colorName("Starlight").colorHex("#f0e4d3")
                                .storage("44mm GPS").condition("BRAND_NEW")
                                .price(new BigDecimal("84900.00"))
                                .stock(20).lowStockThreshold(5)
                                .images(List.of("/watch-se3.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product watchUltra3 = Product.builder()
                .name("Apple Watch Ultra 3")
                .slug("apple-watch-ultra-3")
                .category("Watch")
                .modelNumber("A2987")
                .releaseYear(2025)
                .description("The most rugged and capable Apple Watch, built for endurance and adventure.")
                .status("ACTIVE")
                .specs(Map.of(
                        "display", "Always-On Retina LTPO2 OLED, up to 3000 nits",
                        "chip", "S10 SiP",
                        "case", "49mm titanium",
                        "battery", "Up to 36 hours (72 in low power)"))
                .variants(List.of(
                        Variant.builder().sku("AWU3-49-TIT")
                                .colorName("Natural Titanium").colorHex("#c2bcb2")
                                .storage("49mm GPS + Cellular").condition("BRAND_NEW")
                                .price(new BigDecimal("264900.00"))
                                .stock(8).lowStockThreshold(3)
                                .images(List.of("/watch-ultra3.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        // ── ⭐ iPad products ──
        Product ipadPro = Product.builder()
                .name("iPad Pro")
                .slug("ipad-pro")
                .category("iPad")
                .modelNumber("A2925")
                .releaseYear(2025)
                .description("Thinpossible. The most advanced iPad Pro ever, powered by the M5 chip with Ultra Retina XDR display.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "Apple M5",
                        "display", "13-inch Ultra Retina XDR (tandem OLED)",
                        "camera", "12MP Wide | 10MP Ultra Wide",
                        "battery", "Up to 10 hours"))
                .variants(List.of(
                        Variant.builder().sku("IPADPRO-256-SPBLK")
                                .colorName("Space Black").colorHex("#3b3b3d")
                                .storage("256GB").condition("BRAND_NEW")
                                .price(new BigDecimal("349900.00"))
                                .stock(10).lowStockThreshold(3)
                                .images(List.of("/ipad-pro.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product ipadAir = Product.builder()
                .name("iPad Air")
                .slug("ipad-air")
                .category("iPad")
                .modelNumber("A2589")
                .releaseYear(2025)
                .description("Powerful. Colorful. Wonderful. iPad Air with the M3 chip.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "Apple M3",
                        "display", "11-inch Liquid Retina",
                        "camera", "12MP Wide",
                        "battery", "Up to 10 hours"))
                .variants(List.of(
                        Variant.builder().sku("IPADAIR-128-BLU")
                                .colorName("Blue").colorHex("#a8c5e0")
                                .storage("128GB").condition("BRAND_NEW")
                                .price(new BigDecimal("189900.00"))
                                .stock(14).lowStockThreshold(4)
                                .images(List.of("/ipad-air.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product ipadMini = Product.builder()
                .name("iPad Mini")
                .slug("ipad-mini")
                .category("iPad")
                .modelNumber("A2567")
                .releaseYear(2024)
                .description("Mega power. Mini form. iPad mini with the A17 Pro chip.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "Apple A17 Pro",
                        "display", "8.3-inch Liquid Retina",
                        "camera", "12MP Wide",
                        "battery", "Up to 10 hours"))
                .variants(List.of(
                        Variant.builder().sku("IPADMINI-128-PUR")
                                .colorName("Purple").colorHex("#d9c9e8")
                                .storage("128GB").condition("BRAND_NEW")
                                .price(new BigDecimal("154900.00"))
                                .stock(16).lowStockThreshold(4)
                                .images(List.of("/ipad-mini.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        // ── ⭐ iPhone products — images දැන් .jpg (ඔයාගෙ files වලට match) ──
        Product iphone17Pro = Product.builder()
                .name("iPhone 17 Pro").slug("iphone-17-pro").category("iPhone")
                .modelNumber("A3201").releaseYear(2025)
                .description("The most powerful iPhone ever, with the A19 Pro chip and aluminium unibody design.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "A19 Pro",
                        "display", "6.3-inch Super Retina XDR ProMotion",
                        "camera", "48MP Pro Fusion system",
                        "battery", "Up to 33 hours video playback"))
                .variants(List.of(Variant.builder().sku("IP17PRO-256-BLU")
                        .colorName("Deep Blue").colorHex("#2e3f5c").storage("256GB").condition("BRAND_NEW")
                        .price(new BigDecimal("459900.00")).stock(12).lowStockThreshold(3)
                        .images(List.of("/iphone-17-pro.jpg")).build()))
                .createdAt(Instant.now()).build();

        Product iphone17 = Product.builder()
                .name("iPhone 17").slug("iphone-17").category("iPhone")
                .modelNumber("A3202").releaseYear(2025)
                .description("A total powerhouse, with the A19 chip and a stunning Super Retina XDR display.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "A19",
                        "display", "6.1-inch Super Retina XDR",
                        "camera", "Dual 48MP camera system",
                        "battery", "Up to 30 hours video playback"))
                .variants(List.of(Variant.builder().sku("IP17-128-BLK")
                        .colorName("Black").colorHex("#1d1d1f").storage("128GB").condition("BRAND_NEW")
                        .price(new BigDecimal("329900.00")).stock(15).lowStockThreshold(4)
                        .images(List.of("/iphone-17.jpg")).build()))
                .createdAt(Instant.now()).build();

        Product iphone17Air = Product.builder()
                .name("iPhone 17 Air").slug("iphone-17-air").category("iPhone")
                .modelNumber("A3203").releaseYear(2025)
                .description("Impossibly thin. Remarkably light. The most portable iPhone yet.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "A19",
                        "display", "6.6-inch Super Retina XDR",
                        "camera", "48MP Fusion camera",
                        "design", "Ultra-thin titanium frame"))
                .variants(List.of(Variant.builder().sku("IP17AIR-256-SLV")
                        .colorName("Silver").colorHex("#e3e4e5").storage("256GB").condition("BRAND_NEW")
                        .price(new BigDecimal("389900.00")).stock(10).lowStockThreshold(3)
                        .images(List.of("/iphone-17-air.jpg")).build()))
                .createdAt(Instant.now()).build();

        Product iphone17e = Product.builder()
                .name("iPhone 17e").slug("iphone-17e").category("iPhone")
                .modelNumber("A3204").releaseYear(2025)
                .description("All the essentials of iPhone 17, at a more affordable price.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "A19",
                        "display", "6.1-inch Super Retina XDR",
                        "camera", "48MP Fusion camera",
                        "battery", "Up to 26 hours video playback"))
                .variants(List.of(Variant.builder().sku("IP17E-128-WHT")
                        .colorName("White").colorHex("#f0f0f0").storage("128GB").condition("BRAND_NEW")
                        .price(new BigDecimal("249900.00")).stock(18).lowStockThreshold(5)
                        .images(List.of("/iphone-17e.jpg")).build()))
                .createdAt(Instant.now()).build();

        Product iphone16 = Product.builder()
                .name("iPhone 16").slug("iphone-16").category("iPhone")
                .modelNumber("A3081").releaseYear(2024)
                .description("Built for Apple Intelligence. Powered by the A18 chip with Camera Control.")
                .status("ACTIVE")
                .specs(Map.of(
                        "chip", "A18",
                        "display", "6.1-inch Super Retina XDR",
                        "camera", "48MP Fusion camera",
                        "battery", "Up to 22 hours video playback"))
                .variants(List.of(Variant.builder().sku("IP16-128-TEAL")
                        .colorName("Teal").colorHex("#a3c9c7").storage("128GB").condition("BRAND_NEW")
                        .price(new BigDecimal("279900.00")).stock(20).lowStockThreshold(5)
                        .images(List.of("/iphone-16.jpg")).build()))
                .createdAt(Instant.now()).build();

        // ── Accessories products ──

        Product charger = Product.builder()
                .name("20W USB-C Power Adapter")
                .slug("20w-usb-c-power-adapter")
                .category("Accessories")
                .modelNumber("A2305")
                .releaseYear(2023)
                .description("Fast, efficient charging at home, in the office, or on the go.")
                .status("ACTIVE")
                .specs(Map.of("output", "20W USB-C Power Delivery", "compatibility", "iPhone, iPad, AirPods"))
                .variants(List.of(
                        Variant.builder().sku("ACC-CHG-20W")
                                .colorName("White").colorHex("#ffffff")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("8900.00"))
                                .stock(50).lowStockThreshold(10)
                                .images(List.of("/MGKN4.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product cable = Product.builder()
                .name("USB-C to Lightning Cable (1m)")
                .slug("usb-c-lightning-cable-1m")
                .category("Accessories")
                .modelNumber("A2704")
                .releaseYear(2023)
                .description("Charge and sync your iPhone quickly and conveniently.")
                .status("ACTIVE")
                .specs(Map.of("length", "1 meter", "connector", "USB-C to Lightning"))
                .variants(List.of(
                        Variant.builder().sku("ACC-CBL-1M")
                                .colorName("White").colorHex("#ffffff")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("7400.00"))
                                .stock(60).lowStockThreshold(10)
                                .images(List.of("/MDF14.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product magsafeCharger = Product.builder()
                .name("MagSafe Charger")
                .slug("magsafe-charger")
                .category("Accessories")
                .modelNumber("A2140")
                .releaseYear(2023)
                .description("Wireless charging, perfectly aligned every time.")
                .status("ACTIVE")
                .specs(Map.of("output", "15W MagSafe Wireless", "compatibility", "iPhone 12 and later"))
                .variants(List.of(
                        Variant.builder().sku("ACC-MSC-15W")
                                .colorName("White").colorHex("#ffffff")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("12900.00"))
                                .stock(30).lowStockThreshold(8)
                                .images(List.of("/MT0H3.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product magsafeDuo = Product.builder()
                .name("MagSafe Duo Charger")
                .slug("magsafe-duo-charger")
                .category("Accessories")
                .modelNumber("A2015")
                .releaseYear(2023)
                .description("Charge your iPhone and Apple Watch together, wherever you go.")
                .status("ACTIVE")
                .specs(Map.of("output", "Dual wireless charging", "foldable", "Yes"))
                .variants(List.of(
                        Variant.builder().sku("ACC-MSD-DUO")
                                .colorName("White").colorHex("#ffffff")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("24900.00"))
                                .stock(20).lowStockThreshold(5)
                                .images(List.of("/HSKT2.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product batteryPack = Product.builder()
                .name("MagSafe Battery Pack")
                .slug("magsafe-battery-pack")
                .category("Accessories")
                .modelNumber("A2384")
                .releaseYear(2023)
                .description("Snap-on portable power that goes wherever you go.")
                .status("ACTIVE")
                .specs(Map.of("capacity", "1460mAh", "connector", "MagSafe wireless"))
                .variants(List.of(
                        Variant.builder().sku("ACC-BAT-PACK")
                                .colorName("Blue").colorHex("#a8c5e0")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("17900.00"))
                                .stock(25).lowStockThreshold(6)
                                .images(List.of("/HSK72_AV1.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product magicMouse = Product.builder()
                .name("Magic Mouse")
                .slug("magic-mouse")
                .category("Accessories")
                .modelNumber("A1657")
                .releaseYear(2023)
                .description("Multi-Touch surface with wireless connectivity and rechargeable battery.")
                .status("ACTIVE")
                .specs(Map.of("connectivity", "Bluetooth", "battery", "Rechargeable Li-ion"))
                .variants(List.of(
                        Variant.builder().sku("ACC-MOUSE-BLK")
                                .colorName("Black").colorHex("#1d1d1f")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("22900.00"))
                                .stock(18).lowStockThreshold(5)
                                .images(List.of("/MXK63.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product magicKeyboard = Product.builder()
                .name("Magic Keyboard")
                .slug("magic-keyboard")
                .category("Accessories")
                .modelNumber("A2450")
                .releaseYear(2023)
                .description("Comfortable, quiet typing experience with a numeric keypad.")
                .status("ACTIVE")
                .specs(Map.of("connectivity", "Bluetooth", "layout", "With Numeric Keypad"))
                .variants(List.of(
                        Variant.builder().sku("ACC-KB-NUM")
                                .colorName("Black").colorHex("#1d1d1f")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("29900.00"))
                                .stock(15).lowStockThreshold(4)
                                .images(List.of("/MXK83.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        Product pencilAdapter = Product.builder()
                .name("USB-C to Apple Pencil Adapter")
                .slug("usb-c-apple-pencil-adapter")
                .category("Accessories")
                .modelNumber("A2762")
                .releaseYear(2023)
                .description("Charge your Apple Pencil (1st generation) via USB-C.")
                .status("ACTIVE")
                .specs(Map.of("connector", "USB-C to Lightning"))
                .variants(List.of(
                        Variant.builder().sku("ACC-PEN-ADP")
                                .colorName("White").colorHex("#ffffff")
                                .storage("N/A").condition("BRAND_NEW")
                                .price(new BigDecimal("4900.00"))
                                .stock(35).lowStockThreshold(8)
                                .images(List.of("/MUF82.png"))
                                .build()))
                .createdAt(Instant.now())
                .build();

        // ⭐⭐⭐ FIX: iPhone products 5ක් දැන් list එකට add කරලා තියෙනවා! ⭐⭐⭐
        productRepository.saveAll(List.of(
                iphone, macbook, macbookPro, airpods, airpodsMax,
                watchSeries11, watchSE3, watchUltra3,
                ipadPro, ipadAir, ipadMini,
                iphone17Pro, iphone17, iphone17Air, iphone17e, iphone16,
                charger, cable, magsafeCharger, magsafeDuo, batteryPack,
                magicMouse, magicKeyboard, pencilAdapter));

        System.out.println("✅ Seeded " + productRepository.count() + " products with images!");
    }
}