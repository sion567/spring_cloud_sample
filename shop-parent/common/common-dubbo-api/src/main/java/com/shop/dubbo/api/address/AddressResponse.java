package com.shop.dubbo.api.address;

import lombok.Data;
import java.io.Serializable;

@Data
public class AddressResponse implements Serializable {
    private Long id;
    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
}
