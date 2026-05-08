package com.dailycodework.dreamshops.repository.warehouseTransaction;

import com.dailycodework.dreamshops.dto.warehouseTransaction.WarehouseTransactionList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseTransactionRepositoryCustom {
    Page<WarehouseTransactionList> getWarehouseTransactionWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            Integer companyId
    );
}
