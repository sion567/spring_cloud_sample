package com.shop.dubbo.api.user;

import com.shop.common.entity.Result;

import java.util.Map;

public interface UserDubboService {

    Result<Map<String, Object>> register(RegisterRequest request);

    Result<Map<String, Object>> login(LoginRequest request);

    Result<Map<String, Object>> refreshToken(String token);

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, RegisterRequest request);
}
