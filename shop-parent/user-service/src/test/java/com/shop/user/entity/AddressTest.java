package com.shop.user.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testAddressSetterGetter() {
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

        assertEquals(1L, address.getId());
        assertEquals(1L, address.getUserId());
        assertEquals("Test User", address.getReceiverName());
        assertEquals("13800138000", address.getPhone());
        assertEquals("Guangdong", address.getProvince());
        assertEquals("Shenzhen", address.getCity());
        assertEquals("Nanshan", address.getDistrict());
        assertEquals("Tech Park", address.getDetailAddress());
        assertTrue(address.getIsDefault());
    }
}
