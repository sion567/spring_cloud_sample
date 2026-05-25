package com.shop.user.dubbo;

import com.shop.dubbo.api.address.AddressResponse;
import com.shop.user.entity.Address;
import com.shop.user.mapper.AddressMapper;
import com.shop.user.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressDubboServiceImplTest {

    @Mock
    private AddressService addressService;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressDubboServiceImpl addressDubboService;

    private Address testAddress;
    private AddressResponse testAddressResponse;

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

        testAddressResponse = new AddressResponse();
        testAddressResponse.setId(1L);
        testAddressResponse.setReceiverName("Test User");
        testAddressResponse.setReceiverPhone("13800138000");
        testAddressResponse.setIsDefault(1);
    }

    @Test
    void testGetAddressesByUserId() {
        when(addressService.getAddressesByUserId(1L)).thenReturn(List.of(testAddress));
        when(addressMapper.toDTO(testAddress)).thenReturn(testAddressResponse);

        List<AddressResponse> result = addressDubboService.getAddressesByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getReceiverName());
    }

    @Test
    void testAddAddress() {
        AddressResponse request = new AddressResponse();
        request.setReceiverName("New User");
        request.setReceiverPhone("13900139000");
        request.setIsDefault(0);

        com.shop.user.service.dto.AddressCommand command =
                new com.shop.user.service.dto.AddressCommand();
        command.setReceiverName("New User");
        command.setReceiverPhone("13900139000");
        command.setIsDefault(false);

        when(addressMapper.toCommand(request)).thenReturn(command);
        when(addressService.addAddress(1L, command)).thenReturn(testAddress);
        when(addressMapper.toDTO(testAddress)).thenReturn(testAddressResponse);

        AddressResponse result = addressDubboService.addAddress(1L, request);

        assertNotNull(result);
        verify(addressMapper).toCommand(request);
        verify(addressService).addAddress(1L, command);
    }
}
