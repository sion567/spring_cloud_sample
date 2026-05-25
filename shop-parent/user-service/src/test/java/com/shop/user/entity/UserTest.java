package com.shop.user.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserSetterGetter() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setPhone("13800138000");
        user.setNickname("Test User");
        user.setAvatar("avatar.jpg");
        user.setLevel(2);
        user.setPoints(500);
        user.setStatus(1);

        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("13800138000", user.getPhone());
        assertEquals("Test User", user.getNickname());
        assertEquals("avatar.jpg", user.getAvatar());
        assertEquals(2, user.getLevel());
        assertEquals(500, user.getPoints());
        assertEquals(1, user.getStatus());
    }

    @Test
    void testUserOnCreate() {
        User user = new User();
        user.onCreate();

        assertNotNull(user.getCreateTime());
        assertNotNull(user.getUpdateTime());
    }

    @Test
    void testUserOnUpdate() {
        User user = new User();
        user.onCreate();
        LocalDateTime createTime = user.getCreateTime();

        user.onUpdate();

        assertEquals(createTime, user.getCreateTime());
        assertNotNull(user.getUpdateTime());
    }
}
