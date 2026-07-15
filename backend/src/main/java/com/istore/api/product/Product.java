package com.istore.api.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;             // iPhone 16 Pro

    @Indexed(unique = true)
    private String slug;             // iphone-16-pro (URL එකට)

    private String category;         // iPhone / Mac / iPad / Watch / AirPods / Accessories

    private String modelNumber;      // A3101
    private int releaseYear;
    private String description;

    private String status;           // ACTIVE / COMING_SOON / ARCHIVED

    private Map<String, Object> specs;   // ⭐ flexible! iPhone→chip/camera, Mac→RAM/display

    private List<Variant> variants;      // ⭐ EMBEDDED — MongoDB magic!

    private Instant createdAt;
}