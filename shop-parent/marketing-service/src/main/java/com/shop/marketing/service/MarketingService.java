package com.shop.marketing.service;

import com.shop.marketing.entity.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingService {

    public Coupon createNewUserGiftCoupon(Long userId, String username) {
        log.debug("createNewUserGiftCoupon request: userId={}, username={}", userId, username);
        Coupon coupon = new Coupon();
        coupon.setUserId(userId);
        coupon.setCouponCode(generateCouponCode());
        coupon.setCouponName("新人注册大礼包");
        coupon.setDiscountAmount(new BigDecimal("20.00"));
        coupon.setMinOrderAmount(new BigDecimal("100.00"));
        coupon.setExpireTime(LocalDateTime.now().plusDays(30));
        coupon.setStatus(0);
        coupon.setCreateTime(LocalDateTime.now());

        log.info("Created new user gift coupon for user {} ({}): {}", username, userId, coupon.getCouponCode());
        return coupon;
    }

    private String generateCouponCode() {
        return "NEWUSER" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
