package com.shop.product.controller;

import com.shop.common.entity.PageResult;
import com.shop.common.entity.Result;
import com.shop.product.controller.dto.ProductRequest;
import com.shop.product.controller.dto.StockUpdateRequest;
import com.shop.product.entity.Product;
import com.shop.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Result<PageResult<Product>> getProductList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId) {
        Page<Product> pageResult = productService.getProductPage(page, size, categoryId);
        return Result.success(PageResult.of(
                pageResult.getContent(),
                pageResult.getTotalElements(),
                page,
                size
        ));
    }

    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    @PostMapping
    public Result<Product> createProduct(@Valid @RequestBody ProductRequest request) {
        return Result.success(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return Result.success(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }

    @PutMapping("/{id}/stock")
    public Result<Void> updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
        productService.updateStock(id, request);
        return Result.success();
    }

    @GetMapping("/batch")
    public Result<List<Product>> getProductsByIds(@RequestParam List<Long> ids) {
        return Result.success(productService.getProductsByIds(ids));
    }
}
