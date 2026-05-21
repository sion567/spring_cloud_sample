package com.shop.dubbo.api.user;

import lombok.Data;
import java.io.Serializable;

@Data
public class RegisterRequest implements Serializable {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String nickname;
}
