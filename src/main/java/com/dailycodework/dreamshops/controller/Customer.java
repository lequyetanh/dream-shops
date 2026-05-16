package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.customer.CustomerInfo;
import com.dailycodework.dreamshops.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Customer {
    private final CustomerService customerService;

    @GetMapping("/customer/get-with-paging")
    public ResponseEntity<BaseResultDTO> getCustomerWithPaging(
            Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long companyId
    ){
        BaseResultDTO result = customerService.getCustomerWithPaging(pageable, keyword, companyId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/customer/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = customerService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/customer/create")
    public ResponseEntity<BaseResultDTO> createCustomer(@RequestBody CustomerInfo customerReq) {
        BaseResultDTO result = customerService.createCustomer(customerReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/customer/update")
    public ResponseEntity<BaseResultDTO> updateCustomer(@RequestBody CustomerInfo customerReq){
        BaseResultDTO result = customerService.updateCustomer(customerReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/customer/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteCustomer(@PathVariable(value = "id") Long id){
        BaseResultDTO result = customerService.deleteCustomer(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}/order-history")
    public ResponseEntity<BaseResultDTO> getOrderHistory(@PathVariable Long customerId){
        BaseResultDTO result = customerService.getOrderHistory(customerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}/spending-summary")
    public ResponseEntity<BaseResultDTO> getSpendingSummary(@PathVariable Long customerId){
        BaseResultDTO result = customerService.getSpendingSummary(customerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
