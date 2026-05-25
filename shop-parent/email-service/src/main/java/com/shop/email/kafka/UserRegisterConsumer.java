package com.shop.email.kafka;

import com.shop.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisterConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "user-register", groupId = "email-service")
    public void onUserRegistered(Map<String, Object> message) {
        log.info("Received user registration message: {}", message);

        Long userId = ((Number) message.get("userId")).longValue();
        String username = (String) message.get("username");
        String email = (String) message.get("email");

        if (email != null && !email.isEmpty()) {
            emailService.sendWelcomeEmail(email, username);
            log.info("Welcome email sent to {} for user {}", email, username);
        } else {
            log.warn("No email address found for user {}, skipping welcome email", username);
        }
    }
}
