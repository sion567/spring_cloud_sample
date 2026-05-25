package com.shop.user.service;

import com.shop.common.entity.Result;
import com.shop.common.exception.BusinessException;
import com.shop.common.jwt.JwtUtils;
import com.shop.user.service.dto.LoginCommand;
import com.shop.user.service.dto.RegisterCommand;
import com.shop.user.entity.User;
import com.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${jwt.secret:defaultSecretKeyForDevOnlyPleaseChangeInProduction123456}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    public static final String TOPIC_USER_REGISTER = "user-register";

    public Result<Map<String, Object>> register(RegisterCommand request) {
        log.debug("register request: username={}", request.getUsername());
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(1001, "用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(md5(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setLevel(1);
        user.setPoints(100);

        user = userRepository.save(user);
        log.debug("user saved: id={}", user.getId());

        Map<String, Object> kafkaMessage = new HashMap<>();
        kafkaMessage.put("userId", user.getId());
        kafkaMessage.put("username", user.getUsername());
        kafkaMessage.put("email", user.getEmail());
        kafkaMessage.put("timestamp", System.currentTimeMillis());
        kafkaTemplate.send(TOPIC_USER_REGISTER, user.getId().toString(), kafkaMessage);
        log.debug("kafka message sent: topic={}, userId={}", TOPIC_USER_REGISTER, user.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());
        data.put("level", user.getLevel());
        data.put("points", user.getPoints());
        return Result.success(data);
    }

    public Result<Map<String, Object>> login(LoginCommand request) {
        log.debug("login request: username={}", request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(1002, "用户名或密码错误"));

        if (!md5(request.getPassword()).equals(user.getPassword())) {
            throw new BusinessException(1002, "用户名或密码错误");
        }

        String token = JwtUtils.generateToken(jwtSecret, user.getId(), user.getUsername(), List.of("USER"), jwtExpiration);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "nickname", user.getNickname()
        ));
        log.debug("login success: userId={}", user.getId());
        return Result.success(data);
    }

    public Result<Map<String, Object>> refreshToken(String token) {
        log.debug("refreshToken request");
        if (!JwtUtils.validateToken(jwtSecret, token)) {
            throw new BusinessException(401, "Token无效");
        }

        Long userId = JwtUtils.getUserId(jwtSecret, token);
        String username = JwtUtils.getUsername(jwtSecret, token);

        String newToken = JwtUtils.refreshToken(jwtSecret, token, jwtExpiration);

        Map<String, Object> data = new HashMap<>();
        data.put("token", newToken);
        log.debug("refreshToken success: userId={}", userId);
        return Result.success(data);
    }

    public User getUserById(Long id) {
        log.debug("getUserById request: id={}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
    }

    public User updateUser(Long id, RegisterCommand request) {
        log.debug("updateUser request: id={}", id);
        User user = getUserById(id);
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        return userRepository.save(user);
    }

    private String md5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }
}
