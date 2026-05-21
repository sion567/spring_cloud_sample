package com.shop.order.service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CreateOrderCommand {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "地址ID不能为空")
    private Long addressId;

    @NotEmpty(message = "订单商品不能为空")
    @Valid
    private List<OrderItemCommand> items;

    @Data
    public static class OrderItemCommand {
        @NotNull(message = "商品ID不能为空")
        private Long productId;

        @NotNull(message = "数量不能为空")
        private Integer quantity;
    }
}
