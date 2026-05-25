package com.shop.user.service;

import com.shop.common.exception.BusinessException;
import com.shop.user.entity.Address;
import com.shop.user.repository.AddressRepository;
import com.shop.user.service.dto.AddressCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address testAddress;

    @BeforeEach
    void setUp() {
        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setUserId(1L);
        testAddress.setReceiverName("Test User");
        testAddress.setPhone("13800138000");
        testAddress.setProvince("Guangdong");
        testAddress.setCity("Shenzhen");
        testAddress.setDistrict("Nanshan");
        testAddress.setDetailAddress("Tech Park");
        testAddress.setIsDefault(true);
    }

    @Test
    void testGetAddressesByUserId() {
        when(addressRepository.findByUserId(1L)).thenReturn(List.of(testAddress));

        List<Address> result = addressService.getAddressesByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getReceiverName());
    }

    @Test
    void testAddAddress() {
        AddressCommand command = new AddressCommand();
        command.setReceiverName("New User");
        command.setReceiverPhone("13900139000");
        command.setProvince("Guangdong");
        command.setCity("Shenzhen");
        command.setDistrict("Futian");
        command.setDetailAddress("New Building");
        command.setIsDefault(false);

        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address a = invocation.getArgument(0);
            a.setId(2L);
            return a;
        });

        Address result = addressService.addAddress(1L, command);

        assertNotNull(result);
        assertEquals("New User", result.getReceiverName());
        assertEquals("13900139000", result.getPhone());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void testAddAddressWithDefault() {
        Address existingAddress = new Address();
        existingAddress.setId(1L);
        existingAddress.setUserId(1L);
        existingAddress.setIsDefault(true);

        AddressCommand command = new AddressCommand();
        command.setReceiverName("New User");
        command.setReceiverPhone("13900139000");
        command.setIsDefault(true);

        when(addressRepository.findByUserId(1L)).thenReturn(List.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Address result = addressService.addAddress(1L, command);

        assertNotNull(result);
        assertFalse(existingAddress.getIsDefault());
    }

    @Test
    void testGetAddressById() {
        when(addressRepository.findById(1L)).thenReturn(java.util.Optional.of(testAddress));

        Address result = addressService.getAddressById(1L);

        assertNotNull(result);
        assertEquals("Test User", result.getReceiverName());
    }

    @Test
    void testGetAddressByIdNotFound() {
        when(addressRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> addressService.getAddressById(999L));

        assertEquals(404, exception.getCode());
        assertEquals("地址不存在", exception.getMessage());
    }
}
