package com.shop.user.service;

import com.shop.common.exception.BusinessException;
import com.shop.user.controller.dto.AddressRequest;
import com.shop.user.entity.Address;
import com.shop.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Transactional
    public Address addAddress(Long userId, AddressRequest request) {
        if (request.getIsDefault()) {
            addressRepository.findByUserId(userId).forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        Address address = new Address();
        address.setUserId(userId);
        address.setReceiverName(request.getReceiverName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetailAddress(request.getDetailAddress());
        address.setIsDefault(request.getIsDefault());

        return addressRepository.save(address);
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "地址不存在"));
    }
}
