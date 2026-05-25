package com.shop.marketing.kafka;

import com.shop.marketing.entity.Coupon;
import com.shop.marketing.service.MarketingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisterConsumerTest {

    @Mock
    private MarketingService marketingService;

    @InjectMocks
    private UserRegisterConsumer consumer;

    @Test
    void testOnUserRegistered() {
        Map<String, Object> message = new HashMap<>();
        message.put("userId", 1L);
        message.put("username", "testuser");
        message.put("timestamp", System.currentTimeMillis());

        Coupon coupon = new Coupon();
        coupon.setUserId(1L);
        coupon.setCouponCode("NEWUSER123456");
        coupon.setDiscountAmount(new BigDecimal("20.00"));

        when(marketingService.createNewUserGiftCoupon(anyLong(), anyString())).thenReturn(coupon);

        consumer.onUserRegistered(message);

        verify(marketingService).createNewUserGiftCoupon(1L, "testuser");
    }
}
