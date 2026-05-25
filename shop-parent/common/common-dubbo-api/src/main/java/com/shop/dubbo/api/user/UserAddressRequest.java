package com.shop.dubbo.api.user;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserAddressRequest implements Serializable {
    private Long userId;
}
