package com.shop.dubbo.api.product;

import lombok.Data;
import java.io.Serializable;

@Data
public class StockUpdateRequest implements Serializable {
    private Integer quantity;
}
