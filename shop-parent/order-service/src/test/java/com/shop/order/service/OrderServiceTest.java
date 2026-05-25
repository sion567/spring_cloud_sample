package com.shop.order.service;

import com.shop.common.exception.BusinessException;
import com.shop.dubbo.api.product.ProductResponse;
import com.shop.dubbo.api.product.ProductDubboService;
import com.shop.order.entity.Order;
import com.shop.order.entity.OrderItem;
import com.shop.order.repository.OrderItemRepository;
import com.shop.order.repository.OrderRepository;
import com.shop.order.service.dto.CreateOrderCommand;
import com.shop.order.service.dto.PayOrderCommand;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductDubboService productDubboService;

    @InjectMocks
    private OrderService orderService;

    private Order testOrder;
    private ProductResponse testProduct;

    @BeforeEach
    void setUp() throws Exception {
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setOrderNo("ORD202405011200000001");
        testOrder.setUserId(1L);
        testOrder.setAddressId(1L);
        testOrder.setTotalAmount(new BigDecimal("199.99"));
        testOrder.setPayAmount(new BigDecimal("199.99"));
        testOrder.setStatus(0);
        testOrder.setCreateTime(LocalDateTime.now());

        testProduct = new ProductResponse();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStock(100);
    }

    @Test
    void testCreateOrder() {
        CreateOrderCommand.OrderItemCommand itemCommand = new CreateOrderCommand.OrderItemCommand();
        itemCommand.setProductId(1L);
        itemCommand.setQuantity(2);

        CreateOrderCommand command = new CreateOrderCommand();
        command.setUserId(1L);
        command.setAddressId(1L);
        command.setItems(List.of(itemCommand));

        when(productDubboService.getById(1L).getData()).thenReturn(testProduct);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(1L);
            return o;
        });
        when(orderItemRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createOrder(command);

        assertNotNull(result);
        assertNotNull(result.getOrderNo());
        assertTrue(result.getOrderNo().startsWith("ORD"));
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getAddressId());
        verify(productDubboService).getById(1L);
    }

    @Test
    void testCreateOrderInsufficientStock() {
        ProductResponse lowStockProduct = new ProductResponse();
        lowStockProduct.setId(1L);
        lowStockProduct.setName("Test Product");
        lowStockProduct.setPrice(new BigDecimal("99.99"));
        lowStockProduct.setStock(1);

        CreateOrderCommand.OrderItemCommand itemCommand = new CreateOrderCommand.OrderItemCommand();
        itemCommand.setProductId(1L);
        itemCommand.setQuantity(10);

        CreateOrderCommand command = new CreateOrderCommand();
        command.setUserId(1L);
        command.setAddressId(1L);
        command.setItems(List.of(itemCommand));

        when(productDubboService.getById(1L).getData()).thenReturn(lowStockProduct);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.createOrder(command));

        assertEquals(2002, exception.getCode());
        assertTrue(exception.getMessage().contains("库存不足"));
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(testOrder));

        Order result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals("ORD202405011200000001", result.getOrderNo());
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.getOrderById(999L));

        assertEquals(3001, exception.getCode());
        assertEquals("订单不存在", exception.getMessage());
    }

    @Test
    void testGetOrderByOrderNo() {
        when(orderRepository.findByOrderNo("ORD202405011200000001")).thenReturn(java.util.Optional.of(testOrder));

        Order result = orderService.getOrderByOrderNo("ORD202405011200000001");

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
    }

    @Test
    void testGetUserOrders() {
        Page<Order> orderPage = new PageImpl<>(List.of(testOrder));
        when(orderRepository.findByUserId(eq(1L), any())).thenReturn(orderPage);

        Page<Order> result = orderService.getUserOrders(1L, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetUserOrdersWithStatus() {
        Page<Order> orderPage = new PageImpl<>(List.of(testOrder));
        when(orderRepository.findByUserIdAndStatus(eq(1L), eq(0), any())).thenReturn(orderPage);

        Page<Order> result = orderService.getUserOrders(1L, 0, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testPayOrder() {
        PayOrderCommand command = new PayOrderCommand();
        command.setPayMethod("wechat");
        command.setPayNo("PAY123456");

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        orderService.payOrder(1L, command);

        assertEquals(1, testOrder.getStatus());
        assertEquals("wechat", testOrder.getPayMethod());
        assertEquals("PAY123456", testOrder.getPayNo());
        assertNotNull(testOrder.getPayTime());
    }

    @Test
    void testPayOrderInvalidStatus() {
        testOrder.setStatus(2);

        PayOrderCommand command = new PayOrderCommand();
        command.setPayMethod("wechat");

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(testOrder));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.payOrder(1L, command));

        assertEquals(3002, exception.getCode());
        assertEquals("订单状态不允许支付", exception.getMessage());
    }

    @Test
    void testCancelOrder() {
        testOrder.setStatus(0);

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        orderService.cancelOrder(1L);

        assertEquals(4, testOrder.getStatus());
        assertNotNull(testOrder.getCancelTime());
    }

    @Test
    void testCancelOrderInvalidStatus() {
        testOrder.setStatus(2);

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(testOrder));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.cancelOrder(1L));

        assertEquals(3002, exception.getCode());
    }

    @Test
    void testShipOrder() {
        testOrder.setStatus(1);

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        orderService.shipOrder(1L);

        assertEquals(2, testOrder.getStatus());
        assertNotNull(testOrder.getShipTime());
    }

    @Test
    void testShipOrderInvalidStatus() {
        testOrder.setStatus(0);

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(testOrder));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> orderService.shipOrder(1L));

        assertEquals(3002, exception.getCode());
        assertEquals("订单状态不允许发货", exception.getMessage());
    }
}
