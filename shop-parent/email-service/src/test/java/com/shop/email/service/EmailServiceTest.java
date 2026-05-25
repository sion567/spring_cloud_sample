package com.shop.email.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendWelcomeEmail() {
        String toEmail = "test@example.com";
        String username = "testuser";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendWelcomeEmail(toEmail, username);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendWelcomeEmailWithDifferentUsername() {
        String toEmail = "user@example.com";
        String username = "newuser";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendWelcomeEmail(toEmail, username);

        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
