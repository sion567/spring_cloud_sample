package com.shop.dubbo.api.common;

import com.shop.common.entity.Result;

public interface CrudDubboService<T, ID> {

    Result<T> getById(ID id);

    Object list(Integer page, Integer size);

    Result<T> create(Object request);

    Result<T> update(ID id, Object request);

    Result<Void> delete(ID id);
}
