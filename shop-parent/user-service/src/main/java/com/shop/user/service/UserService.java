package com.shop.user.service;

import com.shop.common.exception.BusinessException;
import com.shop.user.service.dto.LoginCommand;
import com.shop.user.service.dto.RegisterCommand;
import com.shop.user.entity.User;
import com.shop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Map<String, Object> register(RegisterCommand request) {
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

        Map<String, Object> result = new HashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("level", user.getLevel());
        result.put("points", user.getPoints());
        return result;
    }

    public Map<String, Object> login(LoginCommand request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(1002, "用户名或密码错误"));

        if (!md5(request.getPassword()).equals(user.getPassword())) {
            throw new BusinessException(1002, "用户名或密码错误");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("token", "token_" + user.getId() + "_" + System.currentTimeMillis());
        result.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "nickname", user.getNickname()
        ));
        return result;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
    }

    public User updateUser(Long id, RegisterCommand request) {
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
