package com.shop.user.dubbo;

import com.shop.dubbo.api.address.AddressResponse;
import com.shop.dubbo.api.address.AddressDubboService;
import com.shop.user.mapper.AddressMapper;
import com.shop.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService
@RequiredArgsConstructor
public class AddressDubboServiceImpl implements AddressDubboService {
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @Override
    public List<AddressResponse> getAddressesByUserId(Long userId) {
        return addressService.getAddressesByUserId(userId).stream()
                .map(addressMapper::toDTO).toList();
    }

    @Override
    public AddressResponse addAddress(Long userId, AddressResponse request) {
        return addressMapper.toDTO(addressService.addAddress(userId, addressMapper.toCommand(request)));
    }
}
