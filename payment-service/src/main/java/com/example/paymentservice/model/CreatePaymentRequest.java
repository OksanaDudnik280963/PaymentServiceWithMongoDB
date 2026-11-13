package com.example.paymentservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotBlank String customerId,
        @NotBlank String customerName,
        @NotBlank String customerEmail,
        @NotNull @Positive BigDecimal amount,
        @NotBlank String currency,
        @NotBlank @Size(max = 64) String idempotencyKey
) {}
