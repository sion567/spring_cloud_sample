package com.shop.dubbo.api.order;

import com.shop.dubbo.api.common.PageResult;
import jakarta.ws.rs.*;

@Path("/api/order")
public interface OrderDubboService {
    @POST
    OrderResponse createOrder(CreateOrderRequest request);

    @GET
    @Path("/{id}")
    OrderResponse getOrderById(@PathParam("id") Long id);

    @GET
    @Path("/no/{orderNo}")
    OrderResponse getOrderByOrderNo(@PathParam("orderNo") String orderNo);

    @GET
    @Path("/user/{userId}")
    PageResult<OrderResponse> getUserOrders(
            @PathParam("userId") Long userId,
            @QueryParam("status") Integer status,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size);

    @PUT
    @Path("/{id}/pay")
    void payOrder(@PathParam("id") Long id, PayOrderRequest request);

    @PUT
    @Path("/{id}/cancel")
    void cancelOrder(@PathParam("id") Long id);

    @PUT
    @Path("/{id}/ship")
    void shipOrder(@PathParam("id") Long id);
}
