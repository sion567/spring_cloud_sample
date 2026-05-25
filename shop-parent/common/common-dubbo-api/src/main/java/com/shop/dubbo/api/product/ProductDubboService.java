package com.shop.dubbo.api.product;

import com.shop.common.entity.Result;
import com.shop.dubbo.api.common.CrudDubboService;
import com.shop.dubbo.api.common.PageResult;

import java.util.List;

public interface ProductDubboService extends CrudDubboService<ProductResponse, Long, ProductSaveRequest, ProductSaveRequest> {

    PageResult<ProductResponse> getProductPage(Integer page, Integer size, Long categoryId);

    Result<Void> updateStock(Long id, StockUpdateRequest request);

    List<ProductResponse> getProductsByIds(List<Long> ids);
}