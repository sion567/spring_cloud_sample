package com.shop.user.mapper;

import com.shop.dubbo.api.address.AddressResponse;
import com.shop.user.entity.Address;
import com.shop.user.service.dto.AddressCommand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AddressMapperImpl.class)
class AddressMapperTest {

    @Autowired
    private AddressMapper addressMapper;

    @Test
    void testToDTO() {
        Address address = new Address();
        address.setId(1L);
        address.setUserId(1L);
        address.setReceiverName("Test User");
        address.setPhone("13800138000");
        address.setProvince("Guangdong");
        address.setCity("Shenzhen");
        address.setDistrict("Nanshan");
        address.setDetailAddress("Tech Park");
        address.setIsDefault(true);

        AddressResponse response = addressMapper.toDTO(address);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test User", response.getReceiverName());
        assertEquals("13800138000", response.getReceiverPhone());
        assertEquals("Guangdong", response.getProvince());
        assertEquals(1, response.getIsDefault());
    }

    @Test
    void testToEntity() {
        AddressCommand command = new AddressCommand();
        command.setReceiverName("New User");
        command.setReceiverPhone("13900139000");
        command.setProvince("Guangdong");
        command.setCity("Shenzhen");
        command.setDistrict("Futian");
        command.setDetailAddress("New Building");
        command.setIsDefault(false);

        Address address = addressMapper.toEntity(command);

        assertNotNull(address);
        assertEquals("New User", address.getReceiverName());
        assertEquals("13900139000", address.getPhone());
    }

    @Test
    void testToCommand() {
        AddressResponse response = new AddressResponse();
        response.setId(1L);
        response.setReceiverName("Test User");
        response.setReceiverPhone("13800138000");
        response.setIsDefault(1);

        AddressCommand command = addressMapper.toCommand(response);

        assertNotNull(command);
        assertEquals("Test User", command.getReceiverName());
        assertTrue(command.getIsDefault());
    }

    @Test
    void testBooleanToInteger() {
        assertEquals(1, addressMapper.booleanToInteger(true));
        assertEquals(0, addressMapper.booleanToInteger(false));
        assertEquals(0, addressMapper.booleanToInteger(null));
    }

    @Test
    void testIntegerToBoolean() {
        assertTrue(addressMapper.integerToBoolean(1));
        assertFalse(addressMapper.integerToBoolean(0));
        assertFalse(addressMapper.integerToBoolean(null));
    }
}
