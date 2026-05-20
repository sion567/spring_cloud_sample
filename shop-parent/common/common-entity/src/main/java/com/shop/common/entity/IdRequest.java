package com.shop.common.entity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.io.Serializable;

@Data
public class IdRequest implements Serializable {
    @NotNull(message = "id不能为空")
    private Long id;
}
