package com.shop.user.controller;

import com.shop.common.entity.Result;
import com.shop.user.controller.dto.AddressRequest;
import com.shop.user.controller.dto.LoginRequest;
import com.shop.user.controller.dto.RegisterRequest;
import com.shop.user.entity.Address;
import com.shop.user.entity.User;
import com.shop.user.service.AddressService;
import com.shop.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AddressService addressService;

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(userService.register(request));
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(userService.login(request));
    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody RegisterRequest request) {
        return Result.success(userService.updateUser(id, request));
    }

    @GetMapping("/{userId}/address")
    public Result<List<Address>> getAddresses(@PathVariable Long userId) {
        return Result.success(addressService.getAddressesByUserId(userId));
    }

    @PostMapping("/{userId}/address")
    public Result<Address> addAddress(@PathVariable Long userId, @Valid @RequestBody AddressRequest request) {
        return Result.success(addressService.addAddress(userId, request));
    }
}
