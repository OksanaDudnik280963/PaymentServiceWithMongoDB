package com.example.paymentservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        String paymentId,
        String customerId,
        String customerName,
        String customerEmail,
        BigDecimal amount,
        String currency,
        String status,
        String idempotencyKey,
        LocalDateTime createdAt
) {}
