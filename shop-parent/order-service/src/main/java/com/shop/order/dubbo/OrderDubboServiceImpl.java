package com.shop.order.dubbo;

import com.shop.dubbo.api.common.PageResult;
import com.shop.dubbo.api.order.OrderResponse;
import com.shop.dubbo.api.order.CreateOrderRequest;
import com.shop.dubbo.api.order.PayOrderRequest;
import com.shop.dubbo.api.order.OrderDubboService;
import com.shop.dubbo.api.product.ProductDubboService;
import com.shop.order.mapper.OrderMapper;
import com.shop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService
@RequiredArgsConstructor
public class OrderDubboServiceImpl implements OrderDubboService {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @DubboReference
    private ProductDubboService productDubboService;

    @Override
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.debug("createOrder request: userId={}, items={}", request.getUserId(), request.getItems());
        return orderMapper.toDTO(orderService.createOrder(orderMapper.toServiceCreateOrderRequest(request)));
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        log.debug("getOrderById request: id={}", id);
        return orderMapper.toDTO(orderService.getOrderById(id));
    }

    @Override
    public OrderResponse getOrderByOrderNo(String orderNo) {
        log.debug("getOrderByOrderNo request: orderNo={}", orderNo);
        return orderMapper.toDTO(orderService.getOrderByOrderNo(orderNo));
    }

    @Override
    public PageResult<OrderResponse> getUserOrders(Long userId, Integer status, Integer page, Integer size) {
        log.debug("getUserOrders request: userId={}, status={}, page={}, size={}", userId, status, page, size);
        var pageResult = orderService.getUserOrders(userId, status, page, size);
        return PageResult.of(
                pageResult.getContent().stream().map(orderMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                page,
                size
        );
    }

    @Override
    public void payOrder(Long id, PayOrderRequest request) {
        log.debug("payOrder request: id={}, request={}", id, request);
        orderService.payOrder(id, orderMapper.toServicePayOrderRequest(request));
    }

    @Override
    public void cancelOrder(Long id) {
        log.debug("cancelOrder request: id={}", id);
        orderService.cancelOrder(id);
    }

    @Override
    public void shipOrder(Long id) {
        log.debug("shipOrder request: id={}", id);
        orderService.shipOrder(id);
    }
}
