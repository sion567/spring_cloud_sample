package com.shop.common.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResultTest {

    @Test
    void testPageResultOf() {
        var data = List.of("a", "b", "c");
        PageResult<String> result = PageResult.of(data, 10L, 1, 3);

        assertEquals(3, result.getRecords().size());
        assertEquals(10L, result.getTotal());
        assertEquals(1, result.getPage());
        assertEquals(3, result.getSize());
    }

    @Test
    void testPageResultOfWithEmptyList() {
        List<String> data = List.of();
        PageResult<String> result = PageResult.of(data, 0L, 1, 10);

        assertTrue(result.getRecords().isEmpty());
        assertEquals(0L, result.getTotal());
        assertEquals(1, result.getPage());
        assertEquals(10, result.getSize());
    }
}
