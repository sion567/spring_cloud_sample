package com.shop.product.service;

import com.shop.common.exception.BusinessException;
import com.shop.product.entity.Product;
import com.shop.product.repository.ProductRepository;
import com.shop.product.service.dto.ProductSaveCommand;
import com.shop.product.service.dto.StockUpdateCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setStock(100);
        testProduct.setCategoryId(1L);
        testProduct.setImageUrl("http://example.com/image.jpg");
        testProduct.setStatus(1);
        testProduct.setCreateTime(LocalDateTime.now());
        testProduct.setUpdateTime(LocalDateTime.now());
    }

    @Test
    void testGetProductPage() {
        Page<Product> mockPage = new PageImpl<>(List.of(testProduct));
        when(productRepository.findByStatus(eq(1), any(Pageable.class))).thenReturn(mockPage);

        Page<Product> result = productService.getProductPage(1, 10, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findByStatus(eq(1), any(Pageable.class));
    }

    @Test
    void testGetProductPageWithCategoryId() {
        Page<Product> mockPage = new PageImpl<>(List.of(testProduct));
        when(productRepository.findByCategoryId(eq(1L), any(Pageable.class))).thenReturn(mockPage);

        Page<Product> result = productService.getProductPage(1, 10, 1L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(productRepository).findByCategoryId(eq(1L), any(Pageable.class));
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
    }

    @Test
    void testGetProductByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> productService.getProductById(999L));

        assertEquals(2001, exception.getCode());
        assertEquals("商品不存在", exception.getMessage());
    }

    @Test
    void testCreateProduct() {
        ProductSaveCommand command = new ProductSaveCommand();
        command.setName("New Product");
        command.setDescription("New Description");
        command.setPrice(new BigDecimal("199.99"));
        command.setStock(50);
        command.setCategoryId(1L);
        command.setImageUrl("http://example.com/new.jpg");

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        Product result = productService.createProduct(command);

        assertNotNull(result);
        assertEquals("New Product", result.getName());
        assertEquals(1, result.getStatus());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        ProductSaveCommand command = new ProductSaveCommand();
        command.setName("Updated Product");
        command.setPrice(new BigDecimal("299.99"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        Product result = productService.updateProduct(1L, command);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(new BigDecimal("299.99"), result.getPrice());
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.deleteProduct(1L);

        assertEquals(0, testProduct.getStatus());
        verify(productRepository).save(testProduct);
    }

    @Test
    void testUpdateStock() {
        StockUpdateCommand command = new StockUpdateCommand();
        command.setQuantity(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.updateStock(1L, command);

        assertEquals(110, testProduct.getStock());
    }

    @Test
    void testUpdateStockInsufficient() {
        StockUpdateCommand command = new StockUpdateCommand();
        command.setQuantity(-200);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> productService.updateStock(1L, command));

        assertEquals(2002, exception.getCode());
        assertEquals("库存不足", exception.getMessage());
    }

    @Test
    void testGetProductsByIds() {
        List<Product> products = List.of(testProduct);
        when(productRepository.findByIdIn(List.of(1L))).thenReturn(products);

        List<Product> result = productService.getProductsByIds(List.of(1L));

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }
}
