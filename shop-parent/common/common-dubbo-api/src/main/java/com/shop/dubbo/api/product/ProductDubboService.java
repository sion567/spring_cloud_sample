package com.shop.dubbo.api.product;

import com.shop.dubbo.api.common.PageResult;

import java.util.List;

public interface ProductDubboService {

    ProductResponse getProductById(Long id);

    PageResult<ProductResponse> getProductPage(
            Integer page,
            Integer size,
            Long categoryId);

    ProductResponse createProduct(ProductSaveRequest request);

    ProductResponse updateProduct(Long id, ProductSaveRequest request);

    void deleteProduct(Long id);

    void updateStock(Long id, StockUpdateRequest request);

    List<ProductResponse> getProductsByIds(List<Long> ids);
}