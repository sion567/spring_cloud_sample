package com.shop.dubbo.api.user;

import java.util.Map;

public interface UserDubboService {

    Map<String, Object> register(RegisterRequest request);

    Map<String, Object> login(LoginRequest request);

    UserResponse getUserById(Long id); // 3.3 默认自适应路径参数，不写 @PathParam 也能自动对齐

    UserResponse updateUser(Long id, RegisterRequest request);
}
