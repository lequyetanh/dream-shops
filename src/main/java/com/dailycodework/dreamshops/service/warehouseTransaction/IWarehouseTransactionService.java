package com.dailycodework.dreamshops.service.warehouseTransaction;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.warehouseTransaction.WarehouseTransactionReq;
import com.dailycodework.dreamshops.entity.WarehouseTransaction;
import org.springframework.data.domain.Pageable;

public interface IWarehouseTransactionService {
    public BaseResultDTO getWarehouseTransaction(
            Pageable pageable,
            String keyword,
            String fromDate,
            String toDate,
            Integer companyId
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createWarehouseTransaction(WarehouseTransactionReq warehouseTransactionReq);
    public BaseResultDTO updateWarehouseTransaction(WarehouseTransactionReq warehouseTransactionReq);
    public BaseResultDTO deleteWarehouseTransaction(Long id);
}
