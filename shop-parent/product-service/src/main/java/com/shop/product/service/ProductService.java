package com.shop.product.service;

import com.shop.common.exception.BusinessException;
import com.shop.product.controller.dto.ProductRequest;
import com.shop.product.controller.dto.StockUpdateRequest;
import com.shop.product.entity.Product;
import com.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getProductPage(Integer page, Integer size, Long categoryId) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createTime"));
        if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageRequest);
        }
        return productRepository.findByStatus(1, pageRequest);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new BusinessException(2001, "商品不存在"));
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategoryId(request.getCategoryId());
        product.setImageUrl(request.getImageUrl());
        product.setStatus(1);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        Product product = getProductById(id);
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.setStatus(0);
        productRepository.save(product);
    }

    @Transactional
    public void updateStock(Long id, StockUpdateRequest request) {
        Product product = getProductById(id);
        int newStock = product.getStock() + request.getQuantity();
        if (newStock < 0) {
            throw new BusinessException(2002, "库存不足");
        }
        product.setStock(newStock);
        productRepository.save(product);
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }
}
