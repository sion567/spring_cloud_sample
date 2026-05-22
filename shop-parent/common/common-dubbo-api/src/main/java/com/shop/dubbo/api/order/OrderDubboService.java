package com.shop.dubbo.api.order;

import com.shop.dubbo.api.common.PageResult;

public interface OrderDubboService {

    OrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(Long id);

    OrderResponse getOrderByOrderNo(String orderNo);

    PageResult<OrderResponse> getUserOrders(
            Long userId,
            Integer status,
            Integer page,
            Integer size);

    void payOrder(Long id, PayOrderRequest request);

    void cancelOrder(Long id);

    void shipOrder(Long id);
}
