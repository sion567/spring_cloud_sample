package com.shop.order.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PayOrderRequest {
    @NotBlank(message = "支付方式不能为空")
    private String payMethod;

    private String payNo;
}
