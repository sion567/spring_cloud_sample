package com.shop.dubbo.api.common;

import com.shop.common.entity.Result;

public interface CrudDubboService<T, ID, C, U> {

    Result<T> getById(ID id);

    Object listPost(QueryParams params);

    Result<T> create(C request);

    Result<T> update(ID id, U request);

    Result<Void> delete(ID id);
}
