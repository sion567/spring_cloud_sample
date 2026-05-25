package com.shop.user.service;

import com.shop.common.exception.BusinessException;
import com.shop.user.service.dto.AddressCommand;
import com.shop.user.entity.Address;
import com.shop.user.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> getAddressesByUserId(Long userId) {
        log.debug("getAddressesByUserId request: userId={}", userId);
        return addressRepository.findByUserId(userId);
    }

    @Transactional
    public Address addAddress(Long userId, AddressCommand request) {
        log.debug("addAddress request: userId={}, request={}", userId, request);
        if (request.getIsDefault()) {
            addressRepository.findByUserId(userId).forEach(addr -> {
                addr.setIsDefault(false);
                addressRepository.save(addr);
            });
        }

        Address address = new Address();
        address.setUserId(userId);
        address.setReceiverName(request.getReceiverName());
        address.setPhone(request.getReceiverPhone());
        address.setProvince(request.getProvince());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setDetailAddress(request.getDetailAddress());
        address.setIsDefault(request.getIsDefault());

        return addressRepository.save(address);
    }

    public Address getAddressById(Long id) {
        log.debug("getAddressById request: id={}", id);
        return addressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "地址不存在"));
    }
}
