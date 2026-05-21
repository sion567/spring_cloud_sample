package com.shop.order.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PayOrderCommand {
    @NotBlank(message = "支付方式不能为空")
    private String payMethod;

    private String payNo;
}
