package com.shop.dubbo.api.order;

import lombok.Data;
import java.io.Serializable;

@Data
public class PayOrderRequest implements Serializable {
    private String payMethod;
    private String payNo;
}
