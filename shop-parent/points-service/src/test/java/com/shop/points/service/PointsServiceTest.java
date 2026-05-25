package com.shop.points.service;

import com.shop.points.entity.PointsLog;
import com.shop.points.repository.PointsLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointsServiceTest {

    @Mock
    private PointsLogRepository pointsLogRepository;

    @InjectMocks
    private PointsService pointsService;

    @Test
    void testAddRegisterGiftPoints() {
        when(pointsLogRepository.save(any(PointsLog.class))).thenAnswer(invocation -> {
            PointsLog log = invocation.getArgument(0);
            log.setId(1L);
            return log;
        });

        pointsService.addRegisterGiftPoints(1L);

        verify(pointsLogRepository).save(any(PointsLog.class));
    }

    @Test
    void testAddNewUserBonusPoints() {
        when(pointsLogRepository.save(any(PointsLog.class))).thenAnswer(invocation -> {
            PointsLog log = invocation.getArgument(0);
            log.setId(1L);
            return log;
        });

        pointsService.addNewUserBonusPoints(1L);

        verify(pointsLogRepository).save(any(PointsLog.class));
    }

    @Test
    void testGetPointsHistory() {
        PointsLog log = new PointsLog();
        log.setId(1L);
        log.setUserId(1L);
        log.setPoints(100);
        log.setType(1);

        when(pointsLogRepository.findByUserId(1L)).thenReturn(List.of(log));

        List<PointsLog> result = pointsService.getPointsHistory(1L);

        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getPoints());
    }
}
