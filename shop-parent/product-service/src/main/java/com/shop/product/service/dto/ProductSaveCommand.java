package com.shop.product.service.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductSaveCommand {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
    private String imageUrl;
}
