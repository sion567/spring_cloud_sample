package com.shop.dubbo.api.address;

import com.shop.common.entity.Result;
import com.shop.dubbo.api.common.CrudDubboService;

import java.util.List;

public interface AddressDubboService extends CrudDubboService<AddressResponse, Long, AddressResponse, AddressResponse> {

    List<AddressResponse> getAddressesByUserId(Long userId);

    AddressResponse addAddress(Long userId, AddressResponse request);
}
