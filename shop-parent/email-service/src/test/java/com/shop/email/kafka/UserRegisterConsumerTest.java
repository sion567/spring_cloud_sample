package com.shop.email.kafka;

import com.shop.email.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegisterConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserRegisterConsumer consumer;

    @Test
    void testOnUserRegisteredWithEmail() {
        Map<String, Object> message = new HashMap<>();
        message.put("userId", 1L);
        message.put("username", "testuser");
        message.put("email", "test@example.com");
        message.put("timestamp", System.currentTimeMillis());

        doNothing().when(emailService).sendWelcomeEmail("test@example.com", "testuser");

        consumer.onUserRegistered(message);

        verify(emailService).sendWelcomeEmail("test@example.com", "testuser");
    }

    @Test
    void testOnUserRegisteredWithoutEmail() {
        Map<String, Object> message = new HashMap<>();
        message.put("userId", 1L);
        message.put("username", "testuser");
        message.put("email", null);
        message.put("timestamp", System.currentTimeMillis());

        consumer.onUserRegistered(message);

        verify(emailService, never()).sendWelcomeEmail(anyString(), anyString());
    }

    @Test
    void testOnUserRegisteredWithEmptyEmail() {
        Map<String, Object> message = new HashMap<>();
        message.put("userId", 1L);
        message.put("username", "testuser");
        message.put("email", "");
        message.put("timestamp", System.currentTimeMillis());

        consumer.onUserRegistered(message);

        verify(emailService, never()).sendWelcomeEmail(anyString(), anyString());
    }
}
