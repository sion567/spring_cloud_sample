package com.shop.dubbo.api.address;

import java.util.List;

public interface AddressDubboService {
    List<AddressResponse> getAddressesByUserId(Long userId);

    AddressResponse addAddress(Long userId, AddressResponse request);
}
