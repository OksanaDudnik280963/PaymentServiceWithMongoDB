package com.example.notificationservice.controller;

import com.example.common.model.NotificationEvent;
import com.example.notificationservice.model.NotificationDocument;
import com.example.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDocument> createNotification(@Valid @RequestBody NotificationEvent event) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationService.createNotification(event));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification service is running");
    }
}
