package com.shop.dubbo.api.product;

import com.shop.dubbo.api.common.PageResult;
import jakarta.ws.rs.*;
import java.util.List;

public interface ProductDubboService {
//    @GET
//    @Path("/{id}")
    ProductResponse getProductById(@PathParam("id") Long id);

//    @GET
    PageResult<ProductResponse> getProductPage(
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("categoryId") Long categoryId);

    @POST
    ProductResponse createProduct(ProductSaveRequest request);

    @PUT
    @Path("/{id}")
    ProductResponse updateProduct(@PathParam("id") Long id, ProductSaveRequest request);

    @DELETE
    @Path("/{id}")
    void deleteProduct(@PathParam("id") Long id);

    @PUT
    @Path("/{id}/stock")
    void updateStock(@PathParam("id") Long id, StockUpdateRequest request);

    @POST
    @Path("/batch")
    List<ProductResponse> getProductsByIds(List<Long> ids);
}
