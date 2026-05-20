package com.shop.order.client;

import com.shop.common.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProductClient {
    private final RestTemplate restTemplate;

    @Value("${product-service.url:http://localhost:8082}")
    private String productServiceUrl;

    public ProductDTO getProductById(Long id) {
        String url = productServiceUrl + "/api/product/" + id;
        Result<ProductDTO> result = restTemplate.getForObject(url, Result.class);
        if (result != null && result.getData() != null) {
            return result.getData();
        }
        return null;
    }
}