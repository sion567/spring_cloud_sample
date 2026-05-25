package com.shop.marketing.service;

import com.shop.marketing.entity.Coupon;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MarketingServiceTest {

    @InjectMocks
    private MarketingService marketingService;

    @Test
    void testCreateNewUserGiftCoupon() {
        Coupon coupon = marketingService.createNewUserGiftCoupon(1L, "testuser");

        assertNotNull(coupon);
        assertEquals(1L, coupon.getUserId());
        assertEquals("新人注册大礼包", coupon.getCouponName());
        assertEquals(new BigDecimal("20.00"), coupon.getDiscountAmount());
        assertEquals(new BigDecimal("100.00"), coupon.getMinOrderAmount());
        assertEquals(0, coupon.getStatus());
        assertNotNull(coupon.getExpireTime());
        assertNotNull(coupon.getCouponCode());
        assertTrue(coupon.getCouponCode().startsWith("NEWUSER"));
    }

    @Test
    void testCreateNewUserGiftCouponHasCorrectCodeFormat() {
        Coupon coupon1 = marketingService.createNewUserGiftCoupon(1L, "user1");
        Coupon coupon2 = marketingService.createNewUserGiftCoupon(2L, "user2");

        assertNotNull(coupon1.getCouponCode());
        assertNotNull(coupon2.getCouponCode());
        assertEquals(24, coupon1.getCouponCode().length());
    }

    @Test
    void testCreateNewUserGiftCouponExpireTimeIs30Days() {
        Coupon coupon = marketingService.createNewUserGiftCoupon(1L, "testuser");

        assertNotNull(coupon.getExpireTime());
    }
}
