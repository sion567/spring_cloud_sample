package com.shop.dubbo.api.order;

import lombok.Data;
import java.io.Serializable;

@Data
public class UpdateOrderRequest implements Serializable {
    private Long addressId;
    private Integer status;
}
