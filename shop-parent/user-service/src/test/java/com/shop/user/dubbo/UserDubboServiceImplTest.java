package com.shop.user.dubbo;

import com.shop.common.entity.Result;
import com.shop.dubbo.api.user.LoginRequest;
import com.shop.dubbo.api.user.RegisterRequest;
import com.shop.dubbo.api.user.UserResponse;
import com.shop.user.entity.User;
import com.shop.user.mapper.UserMapper;
import com.shop.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDubboServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserDubboServiceImpl userDubboService;

    private User testUser;
    private UserResponse testUserResponse;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setNickname("Test User");
        testUser.setLevel(1);
        testUser.setPoints(100);

        testUserResponse = new UserResponse();
        testUserResponse.setId(1L);
        testUserResponse.setUsername("testuser");
        testUserResponse.setEmail("test@example.com");
        testUserResponse.setNickname("Test User");
    }

    @Test
    void testRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password");

        com.shop.user.service.dto.RegisterCommand command =
                new com.shop.user.service.dto.RegisterCommand();
        command.setUsername("newuser");
        command.setPassword("password");

        when(userMapper.toServiceRegisterRequest(request)).thenReturn(command);
        when(userService.register(command)).thenReturn(Result.success(Map.of("id", 1L, "username", "newuser")));

        Result<Map<String, Object>> result = userDubboService.register(request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals(1L, result.getData().get("id"));
        assertEquals("newuser", result.getData().get("username"));
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        com.shop.user.service.dto.LoginCommand command =
                new com.shop.user.service.dto.LoginCommand();
        command.setUsername("testuser");
        command.setPassword("password");

        when(userMapper.toServiceLoginRequest(request)).thenReturn(command);
        when(userService.login(command)).thenReturn(Result.success(Map.of("token", "token_123")));

        Result<Map<String, Object>> result = userDubboService.login(request);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("token_123", result.getData().get("token"));
    }

    @Test
    void testGetUserById() {
        when(userService.getUserById(1L)).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserResponse);

        UserResponse result = userDubboService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testUpdateUser() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");

        com.shop.user.service.dto.RegisterCommand command =
                new com.shop.user.service.dto.RegisterCommand();
        command.setEmail("new@example.com");

        when(userMapper.toServiceRegisterRequest(request)).thenReturn(command);
        when(userService.updateUser(1L, command)).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserResponse);

        UserResponse result = userDubboService.updateUser(1L, request);

        assertNotNull(result);
        verify(userMapper).toServiceRegisterRequest(request);
        verify(userService).updateUser(1L, command);
    }
}
