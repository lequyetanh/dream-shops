package com.dailycodework.dreamshops.repository.warehouseTransaction;

import com.dailycodework.dreamshops.payload.dto.warehouseTransaction.WarehouseTransactionList;
import com.dailycodework.dreamshops.entity.WarehouseTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface IWarehouseTransactionRepository extends CrudRepository<WarehouseTransaction, Long>, WarehouseTransactionRepositoryCustom {

    Page<WarehouseTransactionList> getWarehouseTransactionWithPaging(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            Integer companyId
    );
}
