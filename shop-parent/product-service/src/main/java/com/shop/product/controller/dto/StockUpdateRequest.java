package com.shop.product.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockUpdateRequest {
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    private String reason;
}
