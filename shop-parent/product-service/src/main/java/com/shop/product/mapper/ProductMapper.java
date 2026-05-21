package com.shop.product.mapper;

import com.shop.dubbo.api.product.ProductResponse;
import com.shop.dubbo.api.product.ProductSaveRequest;
import com.shop.dubbo.api.product.StockUpdateRequest;
import com.shop.product.entity.Product;
import com.shop.product.service.dto.ProductSaveCommand;
import com.shop.product.service.dto.StockUpdateCommand;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductResponse toDTO(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    Product toEntity(ProductSaveCommand request);

    ProductSaveCommand toCommand(ProductSaveRequest request);

    @Mapping(target = "quantity", source = "quantity", defaultValue = "0")
    StockUpdateCommand toCommand(StockUpdateRequest request);
}
