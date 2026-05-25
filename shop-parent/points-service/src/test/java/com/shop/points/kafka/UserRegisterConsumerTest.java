package com.shop.points.kafka;

import com.shop.points.service.PointsService;
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
    private PointsService pointsService;

    @InjectMocks
    private UserRegisterConsumer consumer;

    @Test
    void testOnUserRegistered() {
        Map<String, Object> message = new HashMap<>();
        message.put("userId", 1L);
        message.put("username", "testuser");
        message.put("timestamp", System.currentTimeMillis());

        doNothing().when(pointsService).addRegisterGiftPoints(1L);

        consumer.onUserRegistered(message);

        verify(pointsService).addRegisterGiftPoints(1L);
    }
}
