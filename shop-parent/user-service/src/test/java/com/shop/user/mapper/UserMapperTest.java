package com.shop.user.mapper;

import com.shop.dubbo.api.user.RegisterRequest;
import com.shop.user.entity.User;
import com.shop.user.service.dto.LoginCommand;
import com.shop.user.service.dto.RegisterCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UserMapperImpl.class)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testToDTO() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setNickname("Test User");
        user.setLevel(1);
        user.setPoints(100);
        user.setStatus(1);

        com.shop.dubbo.api.user.UserResponse response = userMapper.toDTO(user);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test User", response.getNickname());
    }

    @Test
    void testToEntityFromRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password");
        request.setEmail("new@example.com");
        request.setPhone("13800138000");
        request.setNickname("New User");

        User user = userMapper.toEntity(request);

        assertNotNull(user);
        assertEquals("newuser", user.getUsername());
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void testToEntityFromRegisterCommand() {
        RegisterCommand command = new RegisterCommand();
        command.setUsername("newuser");
        command.setPassword("password");
        command.setEmail("new@example.com");
        command.setPhone("13800138000");
        command.setNickname("New User");

        User user = userMapper.toEntity(command);

        assertNotNull(user);
        assertEquals("newuser", user.getUsername());
    }

    @Test
    void testToServiceRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password");
        request.setEmail("new@example.com");

        RegisterCommand command = userMapper.toServiceRegisterRequest(request);

        assertNotNull(command);
        assertEquals("newuser", command.getUsername());
        assertEquals("password", command.getPassword());
    }

    @Test
    void testToServiceLoginRequest() {
        com.shop.dubbo.api.user.LoginRequest request = new com.shop.dubbo.api.user.LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        LoginCommand command = userMapper.toServiceLoginRequest(request);

        assertNotNull(command);
        assertEquals("testuser", command.getUsername());
        assertEquals("password", command.getPassword());
    }
}
