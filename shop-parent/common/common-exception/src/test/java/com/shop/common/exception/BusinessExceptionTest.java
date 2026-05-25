package com.shop.common.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void testBusinessExceptionWithMessageOnly() {
        BusinessException exception = new BusinessException("error message");

        assertEquals("error message", exception.getMessage());
        assertEquals(500, exception.getCode());
    }

    @Test
    void testBusinessExceptionWithCodeAndMessage() {
        BusinessException exception = new BusinessException(1001, "custom error");

        assertEquals("custom error", exception.getMessage());
        assertEquals(1001, exception.getCode());
    }

    @Test
    void testBusinessExceptionDifferentCodes() {
        BusinessException e1 = new BusinessException(400, "bad request");
        BusinessException e2 = new BusinessException(404, "not found");
        BusinessException e3 = new BusinessException(500, "server error");

        assertEquals(400, e1.getCode());
        assertEquals(404, e2.getCode());
        assertEquals(500, e3.getCode());
    }
}
