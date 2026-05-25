package com.shop.product.dubbo;

import com.shop.common.exception.BusinessException;
import com.shop.dubbo.api.common.PageResult;
import com.shop.dubbo.api.product.ProductResponse;
import com.shop.dubbo.api.product.ProductSaveRequest;
import com.shop.dubbo.api.product.StockUpdateRequest;
import com.shop.product.entity.Product;
import com.shop.product.mapper.ProductMapper;
import com.shop.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDubboServiceImplTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductDubboServiceImpl productDubboService;

    private Product testProduct;
    private ProductResponse testProductResponse;

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

        testProductResponse = new ProductResponse();
        testProductResponse.setId(1L);
        testProductResponse.setName("Test Product");
        testProductResponse.setDescription("Test Description");
        testProductResponse.setPrice(new BigDecimal("99.99"));
        testProductResponse.setStock(100);
    }

    @Test
    void testGetProductById() {
        when(productService.getProductById(1L)).thenReturn(testProduct);
        when(productMapper.toDTO(testProduct)).thenReturn(testProductResponse);

        ProductResponse result = productDubboService.getProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
        verify(productService).getProductById(1L);
        verify(productMapper).toDTO(testProduct);
    }

    @Test
    void testGetProductPage() {
        Page<Product> productPage = new PageImpl<>(List.of(testProduct));
        when(productService.getProductPage(1, 10, null)).thenReturn(productPage);
        when(productMapper.toDTO(testProduct)).thenReturn(testProductResponse);

        PageResult<ProductResponse> result = productDubboService.getProductPage(1, 10, null);

        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testCreateProduct() {
        ProductSaveRequest request = new ProductSaveRequest();
        request.setName("New Product");
        request.setPrice(new BigDecimal("199.99"));

        com.shop.product.service.dto.ProductSaveCommand command =
                new com.shop.product.service.dto.ProductSaveCommand();
        command.setName("New Product");
        command.setPrice(new BigDecimal("199.99"));

        when(productMapper.toCommand(request)).thenReturn(command);
        when(productService.createProduct(command)).thenReturn(testProduct);
        when(productMapper.toDTO(testProduct)).thenReturn(testProductResponse);

        ProductResponse result = productDubboService.createProduct(request);

        assertNotNull(result);
        verify(productMapper).toCommand(request);
        verify(productService).createProduct(command);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        productDubboService.deleteProduct(1L);

        verify(productService).deleteProduct(1L);
    }

    @Test
    void testUpdateStock() {
        StockUpdateRequest request = new StockUpdateRequest();
        request.setQuantity(50);

        com.shop.product.service.dto.StockUpdateCommand command =
                new com.shop.product.service.dto.StockUpdateCommand();
        command.setQuantity(50);

        when(productMapper.toCommand(request)).thenReturn(command);
        doNothing().when(productService).updateStock(1L, command);

        productDubboService.updateStock(1L, request);

        verify(productMapper).toCommand(request);
        verify(productService).updateStock(1L, command);
    }

    @Test
    void testGetProductsByIds() {
        when(productService.getProductsByIds(List.of(1L))).thenReturn(List.of(testProduct));
        when(productMapper.toDTO(testProduct)).thenReturn(testProductResponse);

        List<ProductResponse> result = productDubboService.getProductsByIds(List.of(1L));

        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }
}
