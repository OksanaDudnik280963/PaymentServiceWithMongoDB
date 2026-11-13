package com.example.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "payments")
@CompoundIndex(name = "idx_customer_created_at", def = "{'customerId': 1, 'createdAt': -1}")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String paymentId;

    private String customerId;
    private String customerName;
    private String customerEmail;
    private BigDecimal amount;
    private String currency;
    private String status;

    @Indexed(unique = true)
    private String idempotencyKey;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
