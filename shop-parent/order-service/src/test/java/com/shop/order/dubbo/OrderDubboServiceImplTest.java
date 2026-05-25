package com.shop.order.dubbo;

import com.shop.common.exception.BusinessException;
import com.shop.dubbo.api.common.PageResult;
import com.shop.dubbo.api.order.CreateOrderRequest;
import com.shop.dubbo.api.order.OrderResponse;
import com.shop.dubbo.api.order.PayOrderRequest;
import com.shop.dubbo.api.product.ProductDubboService;
import com.shop.order.entity.Order;
import com.shop.order.mapper.OrderMapper;
import com.shop.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDubboServiceImplTest {

    @Mock
    private OrderService orderService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ProductDubboService productDubboService;

    @InjectMocks
    private OrderDubboServiceImpl orderDubboService;

    private Order testOrder;
    private OrderResponse testOrderResponse;

    @BeforeEach
    void setUp() {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNo("ORD202405011200000001");
        testOrder.setUserId(1L);
        testOrder.setAddressId(1L);
        testOrder.setTotalAmount(new BigDecimal("199.99"));
        testOrder.setPayAmount(new BigDecimal("199.99"));
        testOrder.setStatus(0);
        testOrder.setCreateTime(LocalDateTime.now());

        testOrderResponse = new OrderResponse();
        testOrderResponse.setId(1L);
        testOrderResponse.setOrderNo("ORD202405011200000001");
        testOrderResponse.setUserId(1L);
        testOrderResponse.setTotalAmount(new BigDecimal("199.99"));
    }

    @Test
    void testCreateOrder() {
        CreateOrderRequest.OrderItemRequest itemRequest = new CreateOrderRequest.OrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(1L);
        request.setAddressId(1L);
        request.setItems(List.of(itemRequest));

        com.shop.order.service.dto.CreateOrderCommand command =
                new com.shop.order.service.dto.CreateOrderCommand();
        command.setUserId(1L);
        command.setAddressId(1L);

        when(orderMapper.toServiceCreateOrderRequest(request)).thenReturn(command);
        when(orderService.createOrder(command)).thenReturn(testOrder);
        when(orderMapper.toDTO(testOrder)).thenReturn(testOrderResponse);

        OrderResponse result = orderDubboService.createOrder(request);

        assertNotNull(result);
        assertEquals("ORD202405011200000001", result.getOrderNo());
    }

    @Test
    void testGetOrderById() {
        when(orderService.getOrderById(1L)).thenReturn(testOrder);
        when(orderMapper.toDTO(testOrder)).thenReturn(testOrderResponse);

        OrderResponse result = orderDubboService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetOrderByOrderNo() {
        when(orderService.getOrderByOrderNo("ORD202405011200000001")).thenReturn(testOrder);
        when(orderMapper.toDTO(testOrder)).thenReturn(testOrderResponse);

        OrderResponse result = orderDubboService.getOrderByOrderNo("ORD202405011200000001");

        assertNotNull(result);
        assertEquals("ORD202405011200000001", result.getOrderNo());
    }

    @Test
    void testGetUserOrders() {
        Page<Order> orderPage = new PageImpl<>(List.of(testOrder));
        when(orderService.getUserOrders(1L, null, 1, 10)).thenReturn(orderPage);
        when(orderMapper.toDTO(testOrder)).thenReturn(testOrderResponse);

        PageResult<OrderResponse> result = orderDubboService.getUserOrders(1L, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
    }

    @Test
    void testPayOrder() {
        PayOrderRequest request = new PayOrderRequest();
        request.setPayMethod("wechat");
        request.setPayNo("PAY123456");

        com.shop.order.service.dto.PayOrderCommand command =
                new com.shop.order.service.dto.PayOrderCommand();
        command.setPayMethod("wechat");
        command.setPayNo("PAY123456");

        when(orderMapper.toServicePayOrderRequest(request)).thenReturn(command);
        doNothing().when(orderService).payOrder(1L, command);

        orderDubboService.payOrder(1L, request);

        verify(orderMapper).toServicePayOrderRequest(request);
        verify(orderService).payOrder(1L, command);
    }

    @Test
    void testCancelOrder() {
        doNothing().when(orderService).cancelOrder(1L);

        orderDubboService.cancelOrder(1L);

        verify(orderService).cancelOrder(1L);
    }

    @Test
    void testShipOrder() {
        doNothing().when(orderService).shipOrder(1L);

        orderDubboService.shipOrder(1L);

        verify(orderService).shipOrder(1L);
    }
}
