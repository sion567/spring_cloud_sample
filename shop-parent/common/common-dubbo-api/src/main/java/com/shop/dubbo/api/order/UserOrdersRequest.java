package com.shop.dubbo.api.order;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserOrdersRequest implements Serializable {
    private Long userId;
    private Integer status;
    private Integer page = 1;
    private Integer size = 10;
}
