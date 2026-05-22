package com.shop.dubbo.api.user;


import org.apache.dubbo.remoting.http12.rest.Mapping;
import java.util.Map;

public interface UserDubboService {

    @Mapping(path = "/api/user/register", method = "[]")
    Map<String, Object> register(RegisterRequest request);

    @Mapping(path = "/api/user/login", method = "POST")
    Map<String, Object> login(LoginRequest request);

    @Mapping(path = "/api/user/{id}", method = "GET")
    UserResponse getUserById(Long id); // 3.3 默认自适应路径参数，不写 @PathParam 也能自动对齐

    @Mapping(path = "/api/user/{id}", method = "PUT")
    UserResponse updateUser(Long id, RegisterRequest request);
}
