package com.shop.dubbo.api.common;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private List<T> data;
    private Long total;
    private Integer page;
    private Integer size;

    public static <T> PageResult<T> of(List<T> data, Long total, Integer page, Integer size) {
        PageResult<T> result = new PageResult<>();
        result.setData(data);
        result.setTotal(total);
        result.setPage(page);
        result.setSize(size);
        return result;
    }
}
