package com.shop.points.kafka;

import com.shop.points.service.PointsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisterConsumer {

    private final PointsService pointsService;

    @KafkaListener(topics = "user-register", groupId = "points-service")
    public void onUserRegistered(Map<String, Object> message) {
        log.info("Received user registration message for points: {}", message);

        Long userId = ((Number) message.get("userId")).longValue();
        String username = (String) message.get("username");

        pointsService.addRegisterGiftPoints(userId);
        log.info("Added register gift points to user {} ({})", username, userId);
    }
}
