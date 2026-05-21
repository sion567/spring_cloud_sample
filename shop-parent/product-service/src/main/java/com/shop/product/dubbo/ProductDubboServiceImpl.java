package com.shop.product.dubbo;

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
public class ProductDubboServiceImpl implements ProductDubboService {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse getProductById(Long id) {
        return productMapper.toDTO(productService.getProductById(id));
    }

    @Override
    public PageResult<ProductResponse> getProductPage(Integer page, Integer size, Long categoryId) {
        var pageResult = productService.getProductPage(page, size, categoryId);
        return PageResult.of(
                pageResult.getContent().stream().map(productMapper::toDTO).toList(),
                pageResult.getTotalElements(),
                page,
                size
        );
    }

    @Override
    public ProductResponse createProduct(ProductSaveRequest request) {
        return productMapper.toDTO(productService.createProduct(productMapper.toCommand(request)));
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductSaveRequest request) {
        return productMapper.toDTO(productService.updateProduct(id, productMapper.toCommand(request)));
    }

    @Override
    public void deleteProduct(Long id) {
        productService.deleteProduct(id);
    }

    @Override
    public void updateStock(Long id, StockUpdateRequest request) {
        productService.updateStock(id, productMapper.toCommand(request));
    }

    @Override
    public List<ProductResponse> getProductsByIds(List<Long> ids) {
        return productService.getProductsByIds(ids).stream().map(productMapper::toDTO).toList();
    }
}
