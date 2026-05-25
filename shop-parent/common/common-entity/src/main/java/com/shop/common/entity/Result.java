package com.shop.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;
    private Instant timestamp;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        result.setTimestamp(Instant.now());
        return result;
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setTimestamp(Instant.now());
        return result;
    }
}
