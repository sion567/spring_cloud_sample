package com.shop.common.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void testSuccessWithoutData() {
        Result<String> result = Result.success();
        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testSuccessWithData() {
        Result<String> result = Result.success("testData");
        assertEquals(200, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals("testData", result.getData());
    }

    @Test
    void testErrorWithMessage() {
        Result<String> result = Result.error("error message");
        assertEquals(500, result.getCode());
        assertEquals("error message", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testErrorWithCodeAndMessage() {
        Result<String> result = Result.error(400, "bad request");
        assertEquals(400, result.getCode());
        assertEquals("bad request", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testResultWithDifferentDataTypes() {
        Result<Integer> intResult = Result.success(123);
        assertEquals(123, intResult.getData());

        Result<Boolean> boolResult = Result.success(true);
        assertTrue(boolResult.getData());
    }
}
