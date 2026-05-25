package com.shop.order.mapper;

import com.shop.dubbo.api.order.CreateOrderRequest;
import com.shop.dubbo.api.order.OrderResponse;
import com.shop.dubbo.api.order.PayOrderRequest;
import com.shop.order.entity.Order;
import com.shop.order.service.dto.CreateOrderCommand;
import com.shop.order.service.dto.PayOrderCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = OrderMapperImpl.class)
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void testToDTO() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderNo("ORD202405011200000001");
        order.setUserId(1L);
        order.setAddressId(1L);
        order.setTotalAmount(new BigDecimal("199.99"));
        order.setPayAmount(new BigDecimal("199.99"));
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());

        OrderResponse response = orderMapper.toDTO(order);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("ORD202405011200000001", response.getOrderNo());
        assertEquals(1L, response.getUserId());
        assertEquals(new BigDecimal("199.99"), response.getTotalAmount());
    }

    @Test
    void testToDTOList() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderNo("ORD001");

        Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderNo("ORD002");

        List<OrderResponse> responses = orderMapper.toDTOList(List.of(order1, order2));

        assertNotNull(responses);
        assertEquals(2, responses.size());
    }

    @Test
    void testToEntity() {
        CreateOrderCommand.OrderItemCommand itemCommand = new CreateOrderCommand.OrderItemCommand();
        itemCommand.setProductId(1L);
        itemCommand.setQuantity(2);

        CreateOrderCommand command = new CreateOrderCommand();
        command.setUserId(1L);
        command.setAddressId(1L);
        command.setItems(List.of(itemCommand));

        Order order = orderMapper.toEntity(command);

        assertNotNull(order);
        assertEquals(1L, order.getUserId());
        assertEquals(1L, order.getAddressId());
    }

    @Test
    void testToServiceCreateOrderRequest() {
        CreateOrderRequest.OrderItemRequest itemRequest = new CreateOrderRequest.OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setAddressId(1L);
        request.setItems(List.of(itemRequest));

        CreateOrderCommand command = orderMapper.toServiceCreateOrderRequest(request);

        assertNotNull(command);
        assertEquals(1L, command.getUserId());
        assertEquals(1L, command.getAddressId());
        assertEquals(1, command.getItems().size());
    }

    @Test
    void testToServicePayOrderRequest() {
        PayOrderRequest request = new PayOrderRequest();
        request.setPayMethod("wechat");
        request.setPayNo("PAY123456");

        PayOrderCommand command = orderMapper.toServicePayOrderRequest(request);

        assertNotNull(command);
        assertEquals("wechat", command.getPayMethod());
        assertEquals("PAY123456", command.getPayNo());
    }
}
