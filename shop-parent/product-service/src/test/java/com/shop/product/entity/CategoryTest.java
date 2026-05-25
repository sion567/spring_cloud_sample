package com.shop.product.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategorySetterGetter() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");
        category.setParentId(0L);
        category.setSort(1);

        assertEquals(1L, category.getId());
        assertEquals("Electronics", category.getName());
        assertEquals(0L, category.getParentId());
        assertEquals(1, category.getSort());
    }

    @Test
    void testCategoryOnCreate() {
        Category category = new Category();
        category.onCreate();

        assertNotNull(category.getCreateTime());
        assertNotNull(category.getUpdateTime());
    }

    @Test
    void testCategoryOnUpdate() {
        Category category = new Category();
        category.onCreate();
        LocalDateTime createTime = category.getCreateTime();

        category.onUpdate();

        assertEquals(createTime, category.getCreateTime());
        assertNotNull(category.getUpdateTime());
    }
}
