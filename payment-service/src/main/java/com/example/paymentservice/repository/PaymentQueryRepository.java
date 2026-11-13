package com.example.paymentservice.repository;

import com.example.paymentservice.model.PaymentDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepository {

    private final MongoTemplate mongoTemplate;

    public List<PaymentDocument> findRecentPaymentsByCustomer(String customerId, int limit) {
        Query query = new Query()
                .addCriteria(Criteria.where("customerId").is(customerId))
                .with(Sort.by(Sort.Direction.DESC, "createdAt"))
                .limit(limit);

        return mongoTemplate.find(query, PaymentDocument.class);
    }

    public long countPaymentsByStatus(String status) {
        Query query = new Query().addCriteria(Criteria.where("status").is(status));
        return mongoTemplate.count(query, PaymentDocument.class);
    }
}
