package com.shop.user.mapper;

import com.shop.dubbo.api.user.UserResponse;
import com.shop.dubbo.api.user.RegisterRequest;
import com.shop.user.entity.User;
import com.shop.user.service.dto.LoginCommand;
import com.shop.user.service.dto.RegisterCommand;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "points", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    User toEntity(RegisterCommand request);

    RegisterCommand toServiceRegisterRequest(RegisterRequest request);

    LoginCommand toServiceLoginRequest(com.shop.dubbo.api.user.LoginRequest request);
}
