package com.example.notificationservice.service;

import com.example.common.model.NotificationEvent;
import com.example.notificationservice.model.NotificationDocument;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationDocument createNotification(NotificationEvent event) {
        return notificationRepository.findByEventId(event.getEventId())
                .orElseGet(() -> {
                    NotificationDocument notification = NotificationDocument.builder()
                            .eventId(event.getEventId())
                            .paymentId(event.getPaymentId())
                            .customerId(event.getCustomerId())
                            .customerEmail(event.getCustomerEmail())
                            .type(event.getType())
                            .message(event.getMessage())
                            .createdAt(LocalDateTime.now())
                            .build();

                    NotificationDocument saved = notificationRepository.save(notification);
                    log.info("Created notification for payment {}", event.getPaymentId());
                    return saved;
                });
    }
}
