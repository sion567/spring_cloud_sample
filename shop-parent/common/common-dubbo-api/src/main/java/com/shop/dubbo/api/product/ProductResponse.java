package com.shop.dubbo.api.product;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String imageUrl;
    private Integer status;
}
