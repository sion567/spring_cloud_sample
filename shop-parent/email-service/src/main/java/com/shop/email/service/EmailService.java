package com.shop.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private static final String FROM_EMAIL = "noreply@shop.com";

    public void sendWelcomeEmail(String toEmail, String username) {
        log.debug("sendWelcomeEmail request: toEmail={}, username={}", toEmail, username);
        String content = buildWelcomeContent(username);

        if ("dev".equals(activeProfile)) {
            System.out.println("========== DEV MODE: EMAIL ==========");
            System.out.println("To: " + toEmail);
            System.out.println("Subject: Welcome to Shop - Dear " + username);
            System.out.println("Content:\n" + content);
            System.out.println("====================================");
        } else {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM_EMAIL);
            message.setTo(toEmail);
            message.setSubject("Welcome to Shop - Dear " + username);
            message.setText(content);
            mailSender.send(message);
            log.info("Welcome email sent successfully to {}", toEmail);
        }
    }

    private String buildWelcomeContent(String username) {
        return String.format("""
            Dear %s,

            Welcome to our Shop family!

            We are thrilled to have you join us. As a new member, you have received:

            🎁 New User Gift Package - Check your account for exclusive coupons!

            What you can do next:
            - Browse our wide range of products
            - Complete your profile
            - Make your first purchase

            If you have any questions, feel free to contact our support team.

            Best regards,
            The Shop Team
            """, username);
    }
}
