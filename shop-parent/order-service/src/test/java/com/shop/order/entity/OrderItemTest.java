package com.shop.order.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testOrderItemSetterGetter() {
        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setOrderId(1L);
        item.setProductId(1L);
        item.setProductName("Test Product");
        item.setPrice(new BigDecimal("99.99"));
        item.setQuantity(2);
        item.setSubtotal(new BigDecimal("199.98"));

        assertEquals(1L, item.getId());
        assertEquals(1L, item.getOrderId());
        assertEquals(1L, item.getProductId());
        assertEquals("Test Product", item.getProductName());
        assertEquals(new BigDecimal("99.99"), item.getPrice());
        assertEquals(2, item.getQuantity());
        assertEquals(new BigDecimal("199.98"), item.getSubtotal());
    }

    @Test
    void testOrderItemOnCreate() {
        OrderItem item = new OrderItem();
        item.onCreate();

        assertNotNull(item.getCreateTime());
    }
}
