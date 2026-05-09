package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.order.OrderInfo;
import com.dailycodework.dreamshops.dto.warehouseTransaction.WarehouseTransactionReq;
import com.dailycodework.dreamshops.service.warehouseTransaction.WarehouseTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class WarehouseTransaction {
    private final WarehouseTransactionService warehouseTransactionService;

    @GetMapping("/warehouse-transaction/get-with-paging")
    public ResponseEntity<BaseResultDTO> getWarehouseTransactionWithPaging(
            Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Integer companyId
    ){
        BaseResultDTO result = warehouseTransactionService.getWarehouseTransaction(
                pageable,
                keyword,
                fromDate,
                toDate,
                companyId
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/warehouse-transaction/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = warehouseTransactionService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/warehouse-transaction/create")
    public ResponseEntity<BaseResultDTO> createWarehouseTransaction(@RequestBody WarehouseTransactionReq warehouseTransactionReq) {
        BaseResultDTO result = warehouseTransactionService.createWarehouseTransaction(warehouseTransactionReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/warehouse-transaction/update")
    public ResponseEntity<BaseResultDTO> updateWarehouseTransaction(@RequestBody WarehouseTransactionReq warehouseTransactionReq){
        BaseResultDTO result = warehouseTransactionService.updateWarehouseTransaction(warehouseTransactionReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/warehouse-transaction/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteWarehouseTransaction(@PathVariable(value = "id") Long id){
        BaseResultDTO result = warehouseTransactionService.deleteWarehouseTransaction(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
