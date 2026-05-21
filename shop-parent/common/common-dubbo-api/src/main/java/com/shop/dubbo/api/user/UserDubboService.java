package com.shop.dubbo.api.user;

import java.util.Map;

@RequestMapping("/api/user")
public interface UserDubboService {
    Map<String, Object> register(RegisterRequest request);

    Map<String, Object> login(LoginRequest request);

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, RegisterRequest request);
}
