package com.shop.common.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdRequestTest {

    @Test
    void testIdRequestSetterGetter() {
        IdRequest request = new IdRequest();
        request.setId(123L);

        assertEquals(123L, request.getId());
    }
}
