package com.shop.marketing.kafka;

import com.shop.marketing.service.MarketingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisterConsumer {

    private final MarketingService marketingService;

    @KafkaListener(topics = "user-register", groupId = "marketing-service")
    public void onUserRegistered(Map<String, Object> message) {
        log.info("Received user registration message for marketing: {}", message);

        Long userId = ((Number) message.get("userId")).longValue();
        String username = (String) message.get("username");

        var coupon = marketingService.createNewUserGiftCoupon(userId, username);
        log.info("New user gift coupon created for user {} ({}): code={}, amount={}",
                username, userId, coupon.getCouponCode(), coupon.getDiscountAmount());
    }
}
