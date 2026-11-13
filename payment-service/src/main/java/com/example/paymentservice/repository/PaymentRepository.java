package com.example.paymentservice.repository;

import com.example.paymentservice.model.PaymentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentRepository extends MongoRepository<PaymentDocument, String> {
    Optional<PaymentDocument> findByIdempotencyKey(String idempotencyKey);
    Optional<PaymentDocument> findByPaymentId(String paymentId);
}
