package com.shop.order.dubbo;

import com.shop.dubbo.api.common.CrudDubboServiceImpl;
import com.shop.common.entity.Result;
import com.shop.dubbo.api.common.PageResult;
import com.shop.dubbo.api.common.QueryParams;
import com.shop.dubbo.api.order.*;
import com.shop.order.mapper.OrderMapper;
import com.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

@DubboService(version = "1.0", timeout = 3000,
        methods = {
                @Method(name = "getById", retries = 2),
                @Method(name = "listPost", retries = 2),
                @Method(name = "user", retries = 2),
                @Method(name = "create", retries = 0),
        })
@RequiredArgsConstructor
public class OrderDubboServiceImpl extends CrudDubboServiceImpl implements OrderDubboService {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Override
    public Result<OrderResponse> getById(Long id) {
        logRequest("getById", "id=", id);
        return Result.success(orderMapper.toDTO(orderService.getOrderById(id)));
    }

    @Override
    public Object listPost(QueryParams params) {
        logRequest("listPost", "params=", params);
        var pageResult = orderService.getUserOrders(null, null, params.getPage(), params.getSize());
        return PageResult.of(
                pageResult.getContent().stream().map(orderMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                params.getPage(),
                params.getSize()
        );
    }

    @Override
    public Result<OrderResponse> create(CreateOrderRequest request) {
        logRequest("create", request);
        return Result.success(orderMapper.toDTO(orderService.createOrder(orderMapper.toServiceCreateOrderRequest(request))));
    }

    @Override
    public Result<OrderResponse> update(Long id, UpdateOrderRequest request) {
        logRequest("update", "id=, request=", id, request);
        throw new UnsupportedOperationException("订单不支持修改");
    }

    @Override
    public Result<Void> delete(Long id) {
        logRequest("delete", "id=", id);
        orderService.cancelOrder(id);
        return success();
    }

    @Override
    public PageResult<OrderResponse> user(UserOrdersRequest request) {
        logRequest("user", "request=", request);
        var pageResult = orderService.getUserOrders(request.getUserId(), request.getStatus(), request.getPage(), request.getSize());
        return PageResult.of(
                pageResult.getContent().stream().map(orderMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                request.getPage(),
                request.getSize()
        );
    }

    public PageResult<OrderResponse> user(Long userId, Integer page, Integer size) {
        logRequest("user", "userId=, page=, size=", userId, page, size);
        var pageResult = orderService.getUserOrders(userId, null, page, size);
        return PageResult.of(
                pageResult.getContent().stream().map(orderMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                page,
                size
        );
    }

    @Override
    public Result<Void> payOrder(Long id, PayOrderRequest request) {
        logRequest("payOrder", "id=, request=", id, request);
        orderService.payOrder(id, orderMapper.toServicePayOrderRequest(request));
        return success();
    }

    @Override
    public Result<Void> cancelOrder(Long id) {
        logRequest("cancelOrder", "id=", id);
        orderService.cancelOrder(id);
        return success();
    }

    @Override
    public Result<Void> shipOrder(Long id) {
        logRequest("shipOrder", "id=", id);
        orderService.shipOrder(id);
        return success();
    }
}
