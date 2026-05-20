package com.shop.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    @NotBlank(message = "联系电话不能为空")
    private String phone;

    @NotBlank(message = "省份不能为空")
    private String province;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "区县不能为空")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    private String detailAddress;

    private Boolean isDefault = false;
}
