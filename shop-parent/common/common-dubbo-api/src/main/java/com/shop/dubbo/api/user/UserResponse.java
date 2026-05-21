package com.shop.dubbo.api.user;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserResponse implements Serializable {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private Integer level;
    private Long points;
}
