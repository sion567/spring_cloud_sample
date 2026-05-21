package com.shop.user.dubbo;

import com.shop.dubbo.api.user.UserResponse;
import com.shop.dubbo.api.user.LoginRequest;
import com.shop.dubbo.api.user.RegisterRequest;
import com.shop.dubbo.api.user.UserDubboService;
import com.shop.user.mapper.UserMapper;
import com.shop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Map;

@DubboService
@RequiredArgsConstructor
public class UserDubboServiceImpl implements UserDubboService {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public Map<String, Object> register(RegisterRequest request) {
        return userService.register(userMapper.toServiceRegisterRequest(request));
    }

    @Override
    public Map<String, Object> login(LoginRequest request) {
        return userService.login(userMapper.toServiceLoginRequest(request));
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userMapper.toDTO(userService.getUserById(id));
    }

    @Override
    public UserResponse updateUser(Long id, RegisterRequest request) {
        return userMapper.toDTO(userService.updateUser(id, userMapper.toServiceRegisterRequest(request)));
    }
}
