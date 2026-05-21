package com.shop.order.service;

import com.shop.common.exception.BusinessException;
import com.shop.dubbo.api.product.ProductResponse;
import com.shop.dubbo.api.product.ProductDubboService;
import com.shop.order.service.dto.CreateOrderCommand;
import com.shop.order.service.dto.PayOrderCommand;
import com.shop.order.entity.Order;
import com.shop.order.entity.OrderItem;
import com.shop.order.repository.OrderItemRepository;
import com.shop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @DubboReference
    private ProductDubboService productDubboService;

    @Transactional
    public Order createOrder(CreateOrderCommand request) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CreateOrderCommand.OrderItemCommand itemRequest : request.getItems()) {
            ProductResponse product = productDubboService.getProductById(itemRequest.getProductId());
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new BusinessException(2002, "商品[" + product.getName() + "]库存不足");
            }

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(itemRequest.getQuantity());
            item.setSubtotal(subtotal);
            orderItems.add(item);
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(request.getUserId());
        order.setAddressId(request.getAddressId());
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setStatus(0);
        order = orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        orderItemRepository.saveAll(orderItems);
        order.setItems(orderItems);

        return order;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(3001, "订单不存在"));
    }

    public Order getOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new BusinessException(3001, "订单不存在"));
    }

    public Page<Order> getUserOrders(Long userId, Integer status, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        if (status != null) {
            return orderRepository.findByUserIdAndStatus(userId, status, pageRequest);
        }
        return orderRepository.findByUserId(userId, pageRequest);
    }

    @Transactional
    public void payOrder(Long id, PayOrderCommand request) {
        Order order = getOrderById(id);
        if (order.getStatus() != 0) {
            throw new BusinessException(3002, "订单状态不允许支付");
        }
        order.setStatus(1);
        order.setPayMethod(request.getPayMethod());
        order.setPayNo(request.getPayNo());
        order.setPayTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = getOrderById(id);
        if (order.getStatus() != 0) {
            throw new BusinessException(3002, "订单状态不允许取消");
        }
        order.setStatus(4);
        order.setCancelTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Transactional
    public void shipOrder(Long id) {
        Order order = getOrderById(id);
        if (order.getStatus() != 1) {
            throw new BusinessException(3002, "订单状态不允许发货");
        }
        order.setStatus(2);
        order.setShipTime(LocalDateTime.now());
        orderRepository.save(order);
    }

    private String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + String.format("%04d", (int) (Math.random() * 10000));
    }
}
