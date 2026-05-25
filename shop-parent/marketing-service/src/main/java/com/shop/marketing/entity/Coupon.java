package com.shop.marketing.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Coupon {
    private Long id;
    private Long userId;
    private String couponCode;
    private String couponName;
    private BigDecimal discountAmount;
    private BigDecimal minOrderAmount;
    private LocalDateTime expireTime;
    private Integer status;
    private LocalDateTime createTime;
}
