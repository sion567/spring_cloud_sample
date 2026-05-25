package com.shop.dubbo.api.order;

import com.shop.common.entity.Result;
import com.shop.dubbo.api.common.CrudDubboService;
import com.shop.dubbo.api.common.PageResult;

public interface OrderDubboService extends CrudDubboService<OrderResponse, Long, CreateOrderRequest, UpdateOrderRequest> {

    PageResult<OrderResponse> user(UserOrdersRequest request);

    Result<Void> payOrder(Long id, PayOrderRequest request);

    Result<Void> cancelOrder(Long id);

    Result<Void> shipOrder(Long id);
}
