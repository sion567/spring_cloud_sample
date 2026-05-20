package com.shop.order.controller;

import com.shop.common.entity.PageResult;
import com.shop.common.entity.Result;
import com.shop.order.controller.dto.CreateOrderRequest;
import com.shop.order.controller.dto.PayOrderRequest;
import com.shop.order.entity.Order;
import com.shop.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Result<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return Result.success(orderService.createOrder(request));
    }

    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable Long id) {
        return Result.success(orderService.getOrderById(id));
    }

    @GetMapping("/no/{orderNo}")
    public Result<Order> getOrderByOrderNo(@PathVariable String orderNo) {
        return Result.success(orderService.getOrderByOrderNo(orderNo));
    }

    @GetMapping("/user/{userId}")
    public Result<PageResult<Order>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Order> pageResult = orderService.getUserOrders(userId, status, page, size);
        return Result.success(PageResult.of(
                pageResult.getContent(),
                pageResult.getTotalElements(),
                page,
                size
        ));
    }

    @PutMapping("/{id}/pay")
    public Result<Void> payOrder(@PathVariable Long id, @Valid @RequestBody PayOrderRequest request) {
        orderService.payOrder(id, request);
        return Result.success();
    }

    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return Result.success();
    }

    @PutMapping("/{id}/ship")
    public Result<Void> shipOrder(@PathVariable Long id) {
        orderService.shipOrder(id);
        return Result.success();
    }
}
