package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.order.OrderInfo;
import com.dailycodework.dreamshops.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Order {
    private final OrderService orderService;

    @GetMapping("/category/get-with-paging")
    public ResponseEntity<BaseResultDTO> getOrderWithPaging(
            Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) Integer status
    ){
        BaseResultDTO result = orderService.getOrderWithPaging(
                pageable,
                keyword,
                fromDate,
                toDate,
                orderCode,
                status
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/category/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = orderService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/category/create")
    public ResponseEntity<BaseResultDTO> createOrder(@RequestBody OrderInfo orderReq) {
        BaseResultDTO result = orderService.createOrder(orderReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/category/update")
    public ResponseEntity<BaseResultDTO> updateOrder(@RequestBody OrderInfo orderReq){
        BaseResultDTO result = orderService.updateOrder(orderReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteOrder(@PathVariable(value = "id") Long id){
        BaseResultDTO result = orderService.deleteOrder(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
