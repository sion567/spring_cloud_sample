package com.shop.user.dubbo;

import com.shop.common.entity.Result;
import com.shop.dubbo.api.user.UserResponse;
import com.shop.dubbo.api.user.LoginRequest;
import com.shop.dubbo.api.user.RegisterRequest;
import com.shop.dubbo.api.user.UserDubboService;
import com.shop.user.mapper.UserMapper;
import com.shop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Map;

@Slf4j
@DubboService(version = "1.0", timeout = 3000, retries = 1)
@RequiredArgsConstructor
public class UserDubboServiceImpl implements UserDubboService {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public Result<Map<String, Object>> register(RegisterRequest request) {
        log.debug("register request: username={}", request.getUsername());
        return userService.register(userMapper.toServiceRegisterRequest(request));
    }

    @Override
    public Result<Map<String, Object>> login(LoginRequest request) {
        log.debug("login request: username={}", request.getUsername());
        return userService.login(userMapper.toServiceLoginRequest(request));
    }

    @Override
    public Result<Map<String, Object>> refreshToken(String token) {
        log.debug("refreshToken request");
        return userService.refreshToken(token);
    }

    @Override
    public UserResponse getUserById(Long id) {
        log.debug("getUserById request: id={}", id);
        return userMapper.toDTO(userService.getUserById(id));
    }

    @Override
    public UserResponse updateUser(Long id, RegisterRequest request) {
        log.debug("updateUser request: id={}", id);
        return userMapper.toDTO(userService.updateUser(id, userMapper.toServiceRegisterRequest(request)));
    }
}
