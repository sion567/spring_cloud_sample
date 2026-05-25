package com.shop.order.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderSetterGetter() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderNo("ORD202405011200000001");
        order.setUserId(1L);
        order.setAddressId(1L);
        order.setTotalAmount(new BigDecimal("199.99"));
        order.setDiscountAmount(new BigDecimal("10.00"));
        order.setPayAmount(new BigDecimal("189.99"));
        order.setStatus(0);
        order.setPayMethod("wechat");
        order.setPayNo("PAY123456");

        assertEquals(1L, order.getId());
        assertEquals("ORD202405011200000001", order.getOrderNo());
        assertEquals(1L, order.getUserId());
        assertEquals(new BigDecimal("199.99"), order.getTotalAmount());
        assertEquals(new BigDecimal("10.00"), order.getDiscountAmount());
        assertEquals(new BigDecimal("189.99"), order.getPayAmount());
        assertEquals(0, order.getStatus());
    }

    @Test
    void testOrderOnCreate() {
        Order order = new Order();
        order.onCreate();

        assertNotNull(order.getCreateTime());
        assertNotNull(order.getUpdateTime());
    }

    @Test
    void testOrderOnUpdate() {
        Order order = new Order();
        order.onCreate();
        LocalDateTime createTime = order.getCreateTime();

        order.onUpdate();

        assertEquals(createTime, order.getCreateTime());
        assertNotNull(order.getUpdateTime());
    }

    @Test
    void testOrderItemsList() {
        Order order = new Order();
        assertNotNull(order.getItems());
        assertTrue(order.getItems() instanceof ArrayList);
    }
}
