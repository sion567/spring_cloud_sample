package com.shop.points.service;

import com.shop.points.entity.PointsLog;
import com.shop.points.repository.PointsLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointsService {

    private final PointsLogRepository pointsLogRepository;

    private static final int TYPE_REGISTER_GIFT = 1;
    private static final int TYPE_NEW_USER_BONUS = 2;

    @Transactional
    public void addRegisterGiftPoints(Long userId) {
        log.debug("addRegisterGiftPoints request: userId={}", userId);
        addPoints(userId, 100, TYPE_REGISTER_GIFT, "注册赠送积分");
        log.info("Added register gift points to user {}", userId);
    }

    @Transactional
    public void addNewUserBonusPoints(Long userId) {
        log.debug("addNewUserBonusPoints request: userId={}", userId);
        addPoints(userId, 200, TYPE_NEW_USER_BONUS, "新人礼包积分");
        log.info("Added new user bonus points to user {}", userId);
    }

    private void addPoints(Long userId, Integer points, Integer type, String description) {
        log.debug("addPoints request: userId={}, points={}, type={}, description={}", userId, points, type, description);
        PointsLog logEntry = new PointsLog();
        logEntry.setUserId(userId);
        logEntry.setPoints(points);
        logEntry.setType(type);
        logEntry.setDescription(description);
        pointsLogRepository.save(logEntry);
    }

    public List<PointsLog> getPointsHistory(Long userId) {
        log.debug("getPointsHistory request: userId={}", userId);
        return pointsLogRepository.findByUserId(userId);
    }
}
