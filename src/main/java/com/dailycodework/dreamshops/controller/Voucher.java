package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.voucher.VoucherInfo;
import com.dailycodework.dreamshops.service.voucher.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Voucher {
    private final VoucherService voucherService;

    // Lấy danh sách voucher có phân trang, lọc theo keyword/code/status/companyId
    @GetMapping("/voucher/get-with-paging")
    public ResponseEntity<BaseResultDTO> getVoucherWithPaging(
            Pageable pageable,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long companyId
    ){
        BaseResultDTO result = voucherService.getVoucherWithPaging(pageable, keyword, code, status, companyId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Lấy chi tiết một voucher theo id
    @GetMapping("/voucher/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = voucherService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Tạo mới voucher
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/voucher/create")
    public ResponseEntity<BaseResultDTO> createVoucher(@RequestBody VoucherInfo voucherReq) {
        BaseResultDTO result = voucherService.createVoucher(voucherReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Cập nhật thông tin voucher (mã, giá trị giảm, hạn dùng, giới hạn lượt dùng...)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/voucher/update")
    public ResponseEntity<BaseResultDTO> updateVoucher(@RequestBody VoucherInfo voucherReq){
        BaseResultDTO result = voucherService.updateVoucher(voucherReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Bật/tắt trạng thái hoạt động của voucher (ACTIVE/INACTIVE)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/voucher/update-status/{id}")
    public ResponseEntity<BaseResultDTO> updateVoucherStatus(
            @PathVariable Long id,
            @RequestParam Integer status
    ){
        BaseResultDTO result = voucherService.updateVoucherStatus(id, status);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Xóa voucher
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/voucher/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteVoucher(@PathVariable(value = "id") Long id){
        BaseResultDTO result = voucherService.deleteVoucher(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Kiểm tra tính hợp lệ và tính trước số tiền giảm của voucher, không trừ lượt dùng
    @GetMapping("/voucher/check")
    public ResponseEntity<BaseResultDTO> checkVoucher(
            @RequestParam String code,
            @RequestParam Long companyId,
            @RequestParam BigDecimal orderAmount
    ){
        BaseResultDTO result = voucherService.checkVoucher(code, companyId, orderAmount);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
