package com.shop.user.dubbo;

import com.shop.dubbo.api.common.CrudDubboServiceImpl;
import com.shop.common.entity.Result;
import com.shop.dubbo.api.address.AddressResponse;
import com.shop.dubbo.api.address.AddressDubboService;
import com.shop.user.mapper.AddressMapper;
import com.shop.user.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService
@RequiredArgsConstructor
public class AddressDubboServiceImpl extends CrudDubboServiceImpl implements AddressDubboService {
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @Override
    public Result<AddressResponse> getById(Long id) {
        logRequest("getById", "id=", id);
        return Result.success(addressMapper.toDTO(addressService.getAddressById(id)));
    }

    @Override
    public Object list(Integer page, Integer size) {
        logRequest("list", "page=, size=", page, size);
        var addresses = addressService.getAddressesByUserId(null);
        return addresses.stream().map(addressMapper::toDTO).toList();
    }

    @Override
    public Result<AddressResponse> create(Object request) {
        logRequest("create", request);
        throw new UnsupportedOperationException("请使用 addAddress 方法添加地址");
    }

    @Override
    public Result<AddressResponse> update(Long id, Object request) {
        logRequest("update", "id=, request=", id, request);
        throw new UnsupportedOperationException("地址不支持修改");
    }

    @Override
    public Result<Void> delete(Long id) {
        logRequest("delete", "id=", id);
        throw new UnsupportedOperationException("地址不支持删除");
    }

    @Override
    public List<AddressResponse> getAddressesByUserId(Long userId) {
        logRequest("getAddressesByUserId", "userId=", userId);
        return addressService.getAddressesByUserId(userId).stream()
                .map(addressMapper::toDTO).toList();
    }

    @Override
    public AddressResponse addAddress(Long userId, AddressResponse request) {
        logRequest("addAddress", "userId=, request=", userId, request);
        return addressMapper.toDTO(addressService.addAddress(userId, addressMapper.toCommand(request)));
    }
}
