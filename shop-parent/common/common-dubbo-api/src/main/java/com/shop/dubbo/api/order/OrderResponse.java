package com.shop.dubbo.api.order;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse implements Serializable {
    private Long id;
    private String orderNo;
    private Long userId;
    private Long addressId;
    private BigDecimal totalAmount;
    private BigDecimal payAmount;
    private Integer status;
    private String payMethod;
    private String payNo;
    private LocalDateTime payTime;
    private LocalDateTime shipTime;
    private LocalDateTime cancelTime;
    private LocalDateTime createTime;
    private List<OrderItemResponse> items;

    @Data
    public static class OrderItemResponse implements Serializable {
        private Long id;
        private Long orderId;
        private Long productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal subtotal;
    }
}
