package com.shop.product.mapper;

import com.shop.dubbo.api.product.ProductResponse;
import com.shop.dubbo.api.product.ProductSaveRequest;
import com.shop.dubbo.api.product.StockUpdateRequest;
import com.shop.product.entity.Product;
import com.shop.product.service.dto.ProductSaveCommand;
import com.shop.product.service.dto.StockUpdateCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ProductMapperImpl.class)
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void testToDTO() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setStock(100);
        product.setCategoryId(1L);
        product.setImageUrl("http://example.com/image.jpg");
        product.setStatus(1);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());

        ProductResponse response = productMapper.toDTO(product);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Product", response.getName());
        assertEquals("Test Description", response.getDescription());
        assertEquals(new BigDecimal("99.99"), response.getPrice());
        assertEquals(100, response.getStock());
    }

    @Test
    void testToEntity() {
        ProductSaveCommand command = new ProductSaveCommand();
        command.setName("New Product");
        command.setDescription("New Description");
        command.setPrice(new BigDecimal("199.99"));
        command.setStock(50);
        command.setCategoryId(1L);
        command.setImageUrl("http://example.com/new.jpg");

        Product product = productMapper.toEntity(command);

        assertNotNull(product);
        assertEquals("New Product", product.getName());
        assertEquals("New Description", product.getDescription());
        assertEquals(new BigDecimal("199.99"), product.getPrice());
        assertEquals(50, product.getStock());
        assertEquals(1L, product.getCategoryId());
    }

    @Test
    void testToCommandFromProductSaveRequest() {
        ProductSaveRequest request = new ProductSaveRequest();
        request.setName("Request Product");
        request.setDescription("Request Description");
        request.setPrice(new BigDecimal("299.99"));
        request.setStock(75);
        request.setCategoryId(2L);
        request.setImageUrl("http://example.com/request.jpg");

        ProductSaveCommand command = productMapper.toCommand(request);

        assertNotNull(command);
        assertEquals("Request Product", command.getName());
        assertEquals("Request Description", command.getDescription());
        assertEquals(new BigDecimal("299.99"), command.getPrice());
        assertEquals(75, command.getStock());
    }

    @Test
    void testToCommandFromStockUpdateRequest() {
        StockUpdateRequest request = new StockUpdateRequest();
        request.setQuantity(50);

        StockUpdateCommand command = productMapper.toCommand(request);

        assertNotNull(command);
        assertEquals(50, command.getQuantity());
    }

    @Test
    void testToCommandFromStockUpdateRequestWithNullQuantity() {
        StockUpdateRequest request = new StockUpdateRequest();

        StockUpdateCommand command = productMapper.toCommand(request);

        assertNotNull(command);
        assertEquals(0, command.getQuantity());
    }
}
