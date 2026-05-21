package com.shop.gateway.service;

import com.shop.dubbo.api.common.PageResult;
import com.shop.dubbo.api.product.ProductResponse;
import com.shop.dubbo.api.product.ProductSaveRequest;
import com.shop.dubbo.api.product.ProductDubboService;
import com.shop.dubbo.api.product.StockUpdateRequest;
import com.shop.dubbo.api.user.UserResponse;
import com.shop.dubbo.api.user.LoginRequest;
import com.shop.dubbo.api.user.RegisterRequest;
import com.shop.dubbo.api.user.UserDubboService;
import com.shop.dubbo.api.address.AddressResponse;
import com.shop.dubbo.api.address.AddressDubboService;
import com.shop.dubbo.api.order.OrderResponse;
import com.shop.dubbo.api.order.CreateOrderRequest;
import com.shop.dubbo.api.order.PayOrderRequest;
import com.shop.dubbo.api.order.OrderDubboService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DubboClientService {

    @DubboReference
    private ProductDubboService productDubboService;

    @DubboReference
    private UserDubboService userDubboService;

    @DubboReference
    private AddressDubboService addressDubboService;

    @DubboReference
    private OrderDubboService orderDubboService;

    public ProductResponse getProductById(Long id) {
        return productDubboService.getProductById(id);
    }

    public PageResult<ProductResponse> getProductPage(Integer page, Integer size, Long categoryId) {
        return productDubboService.getProductPage(page, size, categoryId);
    }

    public ProductResponse createProduct(ProductSaveRequest request) {
        return productDubboService.createProduct(request);
    }

    public ProductResponse updateProduct(Long id, ProductSaveRequest request) {
        return productDubboService.updateProduct(id, request);
    }

    public void deleteProduct(Long id) {
        productDubboService.deleteProduct(id);
    }

    public void updateStock(Long id, StockUpdateRequest request) {
        productDubboService.updateStock(id, request);
    }

    public java.util.List<ProductResponse> getProductsByIds(java.util.List<Long> ids) {
        return productDubboService.getProductsByIds(ids);
    }

    public java.util.Map<String, Object> register(RegisterRequest request) {
        return userDubboService.register(request);
    }

    public java.util.Map<String, Object> login(LoginRequest request) {
        return userDubboService.login(request);
    }

    public UserResponse getUserById(Long id) {
        return userDubboService.getUserById(id);
    }

    public UserResponse updateUser(Long id, RegisterRequest request) {
        return userDubboService.updateUser(id, request);
    }

    public java.util.List<AddressResponse> getAddresses(Long userId) {
        return addressDubboService.getAddressesByUserId(userId);
    }

    public AddressResponse addAddress(Long userId, AddressResponse request) {
        return addressDubboService.addAddress(userId, request);
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        return orderDubboService.createOrder(request);
    }

    public OrderResponse getOrderById(Long id) {
        return orderDubboService.getOrderById(id);
    }

    public OrderResponse getOrderByOrderNo(String orderNo) {
        return orderDubboService.getOrderByOrderNo(orderNo);
    }

    public PageResult<OrderResponse> getUserOrders(Long userId, Integer status, Integer page, Integer size) {
        return orderDubboService.getUserOrders(userId, status, page, size);
    }

    public void payOrder(Long id, PayOrderRequest request) {
        orderDubboService.payOrder(id, request);
    }

    public void cancelOrder(Long id) {
        orderDubboService.cancelOrder(id);
    }

    public void shipOrder(Long id) {
        orderDubboService.shipOrder(id);
    }
}
