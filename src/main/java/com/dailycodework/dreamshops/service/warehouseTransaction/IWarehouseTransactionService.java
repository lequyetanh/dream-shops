package com.dailycodework.dreamshops.service.warehouseTransaction;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.warehouseTransaction.WarehouseTransactionReq;
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
