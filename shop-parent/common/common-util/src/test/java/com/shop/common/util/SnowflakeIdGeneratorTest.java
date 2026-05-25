package com.shop.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnowflakeIdGeneratorTest {

    @Test
    void testSnowflakeIdGeneratorValidParams() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

        assertNotNull(generator);
    }

    @Test
    void testSnowflakeIdGeneratorInvalidWorkerId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SnowflakeIdGenerator(32, 1);
        });
    }

    @Test
    void testSnowflakeIdGeneratorInvalidDatacenterId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new SnowflakeIdGenerator(1, 32);
        });
    }

    @Test
    void testNextIdReturnsUniqueIds() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

        long id1 = generator.nextId();
        long id2 = generator.nextId();

        assertNotEquals(id1, id2);
    }

    @Test
    void testNextIdReturnsPositiveNumbers() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 1);

        for (int i = 0; i < 100; i++) {
            long id = generator.nextId();
            assertTrue(id > 0, "ID should be positive");
        }
    }
}
