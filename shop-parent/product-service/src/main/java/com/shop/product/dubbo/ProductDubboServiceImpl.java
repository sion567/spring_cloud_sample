package com.shop.product.dubbo;

import com.shop.dubbo.api.common.CrudDubboServiceImpl;
import com.shop.common.entity.Result;
import com.shop.dubbo.api.common.PageResult;
import com.shop.dubbo.api.product.ProductResponse;
import com.shop.dubbo.api.product.ProductSaveRequest;
import com.shop.dubbo.api.product.StockUpdateRequest;
import com.shop.dubbo.api.product.ProductDubboService;
import com.shop.product.mapper.ProductMapper;
import com.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService
@RequiredArgsConstructor
public class ProductDubboServiceImpl extends CrudDubboServiceImpl implements ProductDubboService {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Override
    public Result<ProductResponse> getById(Long id) {
        logRequest("getById", "id=", id);
        return Result.success(productMapper.toDTO(productService.getProductById(id)));
    }

    @Override
    public Object list(Integer page, Integer size) {
        logRequest("list", "page=, size=", page, size);
        var pageResult = productService.getProductPage(page, size, null);
        return PageResult.of(
                pageResult.getContent().stream().map(productMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                page,
                size
        );
    }

    @Override
    public Result<ProductResponse> create(Object request) {
        logRequest("create", request);
        return Result.success(productMapper.toDTO(productService.createProduct(productMapper.toCommand((ProductSaveRequest) request))));
    }

    @Override
    public Result<ProductResponse> update(Long id, Object request) {
        logRequest("update", "id=, request=", id, request);
        return Result.success(productMapper.toDTO(productService.updateProduct(id, productMapper.toCommand((ProductSaveRequest) request))));
    }

    @Override
    public Result<Void> delete(Long id) {
        logRequest("delete", "id=", id);
        productService.deleteProduct(id);
        return success();
    }

    @Override
    public PageResult<ProductResponse> getProductPage(Integer page, Integer size, Long categoryId) {
        logRequest("getProductPage", "page=, size=, categoryId=", page, size, categoryId);
        var pageResult = productService.getProductPage(page, size, categoryId);
        return PageResult.of(
                pageResult.getContent().stream().map(productMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                page,
                size
        );
    }

    @Override
    public Result<Void> updateStock(Long id, StockUpdateRequest request) {
        logRequest("updateStock", "id=, request=", id, request);
        productService.updateStock(id, productMapper.toCommand(request));
        return success();
    }

    @Override
    public List<ProductResponse> getProductsByIds(List<Long> ids) {
        logRequest("getProductsByIds", ids);
        return productService.getProductsByIds(ids).stream().map(productMapper::toDTO).toList();
    }
}
