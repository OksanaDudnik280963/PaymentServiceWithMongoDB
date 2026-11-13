package com.example.paymentservice.service;

import com.example.paymentservice.exception.PaymentNotFoundException;
import com.example.paymentservice.model.CreatePaymentRequest;
import com.example.paymentservice.model.PaymentDocument;
import com.example.paymentservice.model.PaymentResponse;
import com.example.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponse createPayment(CreatePaymentRequest request) {
        return paymentRepository.findByIdempotencyKey(request.idempotencyKey())
                .map(this::toResponse)
                .orElseGet(() -> createNewPayment(request));
    }

    public PaymentResponse getPayment(String paymentId) {
        PaymentDocument payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found: " + paymentId));
        return toResponse(payment);
    }

    private PaymentResponse createNewPayment(CreatePaymentRequest request) {
        PaymentDocument payment = PaymentDocument.builder()
                .paymentId(UUID.randomUUID().toString())
                .customerId(request.customerId())
                .customerName(request.customerName())
                .customerEmail(request.customerEmail())
                .amount(request.amount())
                .currency(request.currency())
                .status("CREATED")
                .idempotencyKey(request.idempotencyKey())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PaymentDocument saved = paymentRepository.save(payment);
        log.info("Created payment {} for customer {}", saved.getPaymentId(), saved.getCustomerId());
        return toResponse(saved);
    }

    private PaymentResponse toResponse(PaymentDocument payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getCustomerId(),
                payment.getCustomerName(),
                payment.getCustomerEmail(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getIdempotencyKey(),
                payment.getCreatedAt()
        );
    }
}
