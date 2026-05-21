package com.shop.order.mapper;

import com.shop.dubbo.api.order.OrderResponse;
import com.shop.dubbo.api.order.CreateOrderRequest;
import com.shop.dubbo.api.order.PayOrderRequest;
import com.shop.order.entity.Order;
import com.shop.order.entity.OrderItem;
import com.shop.order.service.dto.CreateOrderCommand;
import com.shop.order.service.dto.PayOrderCommand;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderResponse toDTO(Order order);

    List<OrderResponse> toDTOList(List<Order> orders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderNo", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "payMethod", ignore = true)
    @Mapping(target = "payNo", ignore = true)
    @Mapping(target = "payTime", ignore = true)
    @Mapping(target = "shipTime", ignore = true)
    @Mapping(target = "receiveTime", ignore = true)
    @Mapping(target = "cancelTime", ignore = true)
    @Mapping(target = "cancelReason", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "items", ignore = true)
    Order toEntity(CreateOrderCommand request);

    CreateOrderCommand toServiceCreateOrderRequest(CreateOrderRequest request);

    PayOrderCommand toServicePayOrderRequest(PayOrderRequest request);
}
