package com.shop.gateway.filter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GatewayLogFilterTest {

    @Test
    void testGatewayLogFilterGetOrder() {
        GatewayLogFilter filter = new GatewayLogFilter();
        assertEquals(-1, filter.getOrder());
    }
}
