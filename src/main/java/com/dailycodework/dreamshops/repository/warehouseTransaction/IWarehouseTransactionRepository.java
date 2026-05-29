package com.dailycodework.dreamshops.repository.warehouseTransaction;

import com.dailycodework.dreamshops.entity.WarehouseTransaction;
import org.springframework.data.repository.CrudRepository;

public interface IWarehouseTransactionRepository extends CrudRepository<WarehouseTransaction, Long>, WarehouseTransactionRepositoryCustom {

}
