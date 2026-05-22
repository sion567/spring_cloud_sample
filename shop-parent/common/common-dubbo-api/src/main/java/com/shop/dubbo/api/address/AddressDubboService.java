package com.shop.dubbo.api.address;

import jakarta.ws.rs.*;
import java.util.List;

@Path("/api/address")
public interface AddressDubboService {
    @GET
    @Path("/user/{userId}")
    List<AddressResponse> getAddressesByUserId(@PathParam("userId") Long userId);

    @POST
    @Path("/user/{userId}")
    AddressResponse addAddress(@PathParam("userId") Long userId, AddressResponse request);
}
