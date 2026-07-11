package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
import com.dailycodework.dreamshops.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Order {
    private final OrderService orderService;

//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/order/get-with-paging")
    public ResponseEntity<BaseResultDTO> getOrderWithPaging(
            Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer companyId
    ){
        BaseResultDTO result = orderService.getOrderWithPaging(
                pageable,
                keyword,
                fromDate,
                toDate,
                orderCode,
                status,
                companyId
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/order/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = orderService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping("/order/create")
    public ResponseEntity<BaseResultDTO> createOrder(@RequestBody OrderInfo orderReq) {
        BaseResultDTO result = orderService.createOrder(orderReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/order/update")
    public ResponseEntity<BaseResultDTO> updateOrder(@RequestBody OrderInfo orderReq){
        BaseResultDTO result = orderService.updateOrder(orderReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping("/order/update-status/{id}")
    public ResponseEntity<BaseResultDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Integer status
    ){
        BaseResultDTO result = orderService.updateOrderStatus(id, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/order/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteOrder(@PathVariable(value = "id") Long id){
        BaseResultDTO result = orderService.deleteOrder(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Nhập đơn hàng hàng loạt từ file Excel (mỗi dòng là một sản phẩm, các dòng cùng "Mã đơn hàng" được gộp thành 1 đơn)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping(value = "/order/import-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResultDTO> importOrdersFromExcel(@RequestParam("file") MultipartFile file) throws IOException {
        BaseResultDTO result = orderService.importOrdersFromExcel(file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Xuất danh sách đơn hàng (theo cùng bộ lọc với get-with-paging) ra file Excel
    @GetMapping("/order/export-excel")
    public ResponseEntity<byte[]> exportOrdersToExcel(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer companyId
    ) throws IOException {
        byte[] content = orderService.exportOrdersToExcel(keyword, fromDate, toDate, orderCode, status, companyId);
        return buildFileResponse(content, "orders.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // Xuất danh sách đơn hàng (theo cùng bộ lọc với get-with-paging) ra file PDF
    @GetMapping("/order/export-pdf")
    public ResponseEntity<byte[]> exportOrdersToPdf(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer companyId
    ) throws IOException {
        byte[] content = orderService.exportOrdersToPdf(keyword, fromDate, toDate, orderCode, status, companyId);
        return buildFileResponse(content, "orders.pdf", MediaType.APPLICATION_PDF_VALUE);
    }

    // Xuất danh sách đơn hàng (theo cùng bộ lọc với get-with-paging) ra file XML
    @GetMapping("/order/export-xml")
    public ResponseEntity<byte[]> exportOrdersToXml(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer companyId
    ) throws IOException {
        byte[] content = orderService.exportOrdersToXml(keyword, fromDate, toDate, orderCode, status, companyId);
        return buildFileResponse(content, "orders.xml", MediaType.APPLICATION_XML_VALUE);
    }

    private ResponseEntity<byte[]> buildFileResponse(byte[] content, String filename, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
}
