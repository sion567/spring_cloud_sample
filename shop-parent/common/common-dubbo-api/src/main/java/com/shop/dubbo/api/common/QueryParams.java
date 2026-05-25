package com.shop.dubbo.api.common;

import lombok.Data;

@Data
public class QueryParams {
    private Integer page = 1;
    private Integer size = 10;
}
