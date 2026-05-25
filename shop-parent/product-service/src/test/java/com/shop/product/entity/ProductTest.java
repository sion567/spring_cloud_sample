package com.shop.product.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductSetterGetter() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(100);
        product.setCategoryId(1L);
        product.setImageUrl("http://example.com/image.jpg");
        product.setStatus(1);

        assertEquals(1L, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Test Description", product.getDescription());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals(100, product.getStock());
        assertEquals(1L, product.getCategoryId());
        assertEquals("http://example.com/image.jpg", product.getImageUrl());
        assertEquals(1, product.getStatus());
    }

    @Test
    void testProductOnCreate() {
        Product product = new Product();
        product.onCreate();

        assertNotNull(product.getCreateTime());
        assertNotNull(product.getUpdateTime());
    }

    @Test
    void testProductOnUpdate() {
        Product product = new Product();
        product.onCreate();
        LocalDateTime createTime = product.getCreateTime();

        product.onUpdate();

        assertEquals(createTime, product.getCreateTime());
        assertNotNull(product.getUpdateTime());
    }
}
