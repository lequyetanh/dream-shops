package com.dailycodework.dreamshops.controller;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.company.CompanyInfo;
import com.dailycodework.dreamshops.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Company {
    private final CompanyService companyService;

    @GetMapping("/company/get-with-paging")
    public ResponseEntity<BaseResultDTO> getCompanyWithPaging(
            @org.springdoc.api.annotations.ParameterObject Pageable pageable,
            @RequestParam(required = false) String keyword
    ){
        BaseResultDTO result = companyService.getCompanyWithPaging(
                pageable,
                keyword
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/company/find-by-id/{id}")
    public ResponseEntity<BaseResultDTO> findById(@PathVariable(value = "id") Long id){
        BaseResultDTO result = companyService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/company/create")
    public ResponseEntity<BaseResultDTO> createCompany(@RequestBody CompanyInfo companyReq) {
        BaseResultDTO result = companyService.createCompany(companyReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/company/update")
    public ResponseEntity<BaseResultDTO> updateCompany(@RequestBody CompanyInfo companyReq){
        BaseResultDTO result = companyService.updateCompany(companyReq);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/company/delete/{id}")
    public ResponseEntity<BaseResultDTO> deleteCompany(@PathVariable(value = "id") Long id){
        BaseResultDTO result = companyService.deleteCompany(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
