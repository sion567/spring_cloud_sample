package com.shop.dubbo.api.address;

import com.shop.common.entity.Result;
import com.shop.dubbo.api.common.CrudDubboService;
import com.shop.dubbo.api.user.UserAddressRequest;

import java.util.List;

public interface AddressDubboService extends CrudDubboService<AddressResponse, Long, AddressResponse, AddressResponse> {

    List<AddressResponse> getAddresses(UserAddressRequest request);

    AddressResponse addAddress(Long userId, AddressResponse request);
}
