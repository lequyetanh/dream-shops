package com.dailycodework.dreamshops.util;

import jakarta.persistence.Query;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

public class Common {

    public static void setParamsWithPageable(
            @NotNull Query query,
            Map<String, Object> params,
            @NotNull Pageable pageable,
            @NotNull Number total
    ) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

}
