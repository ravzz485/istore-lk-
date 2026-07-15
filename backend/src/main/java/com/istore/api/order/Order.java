package com.istore.api.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    @Indexed(unique = true)
    private String orderNo;          // #IS-2026-00001

    private String customerId;

    private List<OrderItem> items;   // embedded — order එකත් එක්කම කියවනවා

    private String fulfilmentMethod; // DELIVERY / PICKUP
    private String deliveryAddress;
    private String paymentMethod;    // COD / BANK_TRANSFER

    private String status;           // PENDING → CONFIRMED → ... → COMPLETED
    private BigDecimal total;

    private Instant createdAt;
}