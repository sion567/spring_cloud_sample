package com.shop.dubbo.api.common;

import com.shop.common.entity.Result;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CrudDubboServiceImpl {

    protected Result<Void> success() {
        return Result.success();
    }

    protected void logRequest(String methodName, Object... params) {
        if (params == null || params.length == 0) {
            log.debug("{} request", methodName);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(methodName).append(" request: ");
            for (int i = 0; i < params.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append("arg").append(i).append("={}");
            }
            log.debug(sb.toString(), params);
        }
    }

    protected void logRequest(String methodName, String paramName, Object paramValue) {
        log.debug("{} request: {}{}", methodName, paramName, paramValue);
    }

    protected QueryParams queryParams(Integer page, Integer size) {
        QueryParams params = new QueryParams();
        if (page != null) params.setPage(page);
        if (size != null) params.setSize(size);
        return params;
    }
}
