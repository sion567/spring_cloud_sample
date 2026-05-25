package com.shop.order.dubbo;

import com.shop.dubbo.api.common.CrudDubboServiceImpl;
import com.shop.common.entity.Result;
import com.shop.dubbo.api.common.PageResult;
import com.shop.dubbo.api.order.OrderResponse;
import com.shop.dubbo.api.order.CreateOrderRequest;
import com.shop.dubbo.api.order.PayOrderRequest;
import com.shop.dubbo.api.order.OrderDubboService;
import com.shop.dubbo.api.product.ProductDubboService;
import com.shop.order.mapper.OrderMapper;
import com.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class OrderDubboServiceImpl extends CrudDubboServiceImpl implements OrderDubboService {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @DubboReference
    private ProductDubboService productDubboService;

    @Override
    public Result<OrderResponse> getById(Long id) {
        logRequest("getById", "id=", id);
        return Result.success(orderMapper.toDTO(orderService.getOrderById(id)));
    }

    @Override
    public Object list(Integer page, Integer size) {
        logRequest("list", "page=, size=", page, size);
        var pageResult = orderService.getUserOrders(null, null, page, size);
        return PageResult.of(
                pageResult.getContent().stream().map(orderMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                page,
                size
        );
    }

    @Override
    public Result<OrderResponse> create(Object request) {
        logRequest("create", request);
        return Result.success(orderMapper.toDTO(orderService.createOrder(orderMapper.toServiceCreateOrderRequest((CreateOrderRequest) request))));
    }

    @Override
    public Result<OrderResponse> update(Long id, Object request) {
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
    public PageResult<OrderResponse> getUserOrders(Long userId, Integer status, Integer page, Integer size) {
        logRequest("getUserOrders", "userId=, status=, page=, size=", userId, status, page, size);
        var pageResult = orderService.getUserOrders(userId, status, page, size);
        return PageResult.of(
                pageResult.getContent().stream().map(orderMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                page,
                size
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
