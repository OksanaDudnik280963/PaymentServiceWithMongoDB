package com.example.notificationservice.repository;

import com.example.notificationservice.model.NotificationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface NotificationRepository extends MongoRepository<NotificationDocument, String> {
    Optional<NotificationDocument> findByEventId(String eventId);
}
