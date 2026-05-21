package com.shop.dubbo.api.order;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class CreateOrderRequest implements Serializable {
    private Long userId;
    private Long addressId;
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest implements Serializable {
        private Long productId;
        private Integer quantity;
    }
}
