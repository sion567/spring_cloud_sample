package com.shop.user.service;

import com.shop.common.entity.Result;
import com.shop.common.exception.BusinessException;
import com.shop.user.entity.User;
import com.shop.user.repository.UserRepository;
import com.shop.user.service.dto.LoginCommand;
import com.shop.user.service.dto.RegisterCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(userService, "jwtSecret", "test-secret-key-for-jwt-token-signing-must-be-long-enough-123456");
        ReflectionTestUtils.setField(userService, "jwtExpiration", 86400000L);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("e10adc3949ba59abbe56e057f20f883e");
        testUser.setEmail("test@example.com");
        testUser.setPhone("13800138000");
        testUser.setNickname("Test User");
        testUser.setLevel(1);
        testUser.setPoints(100);
        testUser.setStatus(1);
    }

    @Test
    void testRegister() {
        RegisterCommand command = new RegisterCommand();
        command.setUsername("newuser");
        command.setPassword("password123");
        command.setEmail("new@example.com");
        command.setNickname("New User");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });
        doNothing().when(kafkaTemplate).send(anyString(), anyString(), anyMap());

        Result<Map<String, Object>> result = userService.register(command);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        Map<String, Object> data = result.getData();
        assertEquals(2L, data.get("id"));
        assertEquals("newuser", data.get("username"));
        assertEquals("New User", data.get("nickname"));
        assertEquals(1, data.get("level"));
        assertEquals(100, data.get("points"));
        verify(kafkaTemplate).send(eq("user-register"), eq("2"), anyMap());
    }

    @Test
    void testRegisterUsernameExists() {
        RegisterCommand command = new RegisterCommand();
        command.setUsername("existinguser");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.register(command));

        assertEquals(1001, exception.getCode());
        assertEquals("用户名已存在", exception.getMessage());
    }

    @Test
    void testLogin() {
        LoginCommand command = new LoginCommand();
        command.setUsername("testuser");
        command.setPassword("123456");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        Result<Map<String, Object>> result = userService.login(command);

        assertNotNull(result);
        assertEquals(200, result.getCode());
        Map<String, Object> data = result.getData();
        assertNotNull(data.get("token"));
        assertTrue(((String) data.get("token")).startsWith("eyJ"));
        assertNotNull(data.get("user"));
    }

    @Test
    void testLoginUserNotFound() {
        LoginCommand command = new LoginCommand();
        command.setUsername("nonexistent");
        command.setPassword("password");

        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.login(command));

        assertEquals(1002, exception.getCode());
        assertEquals("用户名或密码错误", exception.getMessage());
    }

    @Test
    void testLoginWrongPassword() {
        LoginCommand command = new LoginCommand();
        command.setUsername("testuser");
        command.setPassword("wrongpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.login(command));

        assertEquals(1002, exception.getCode());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> userService.getUserById(999L));

        assertEquals(404, exception.getCode());
        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void testUpdateUser() {
        RegisterCommand command = new RegisterCommand();
        command.setEmail("newemail@example.com");
        command.setNickname("Updated Nickname");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, command);

        assertNotNull(result);
        assertEquals("newemail@example.com", testUser.getEmail());
        assertEquals("Updated Nickname", testUser.getNickname());
    }
}
