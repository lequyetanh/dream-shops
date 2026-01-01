package com.dailycodework.dreamshops.service.company;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.company.CompanyInfo;
import org.springframework.data.domain.Pageable;

public interface ICompanyService {
    public BaseResultDTO getCompanyWithPaging(
            Pageable pageable,
            String keyword
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createCompany(CompanyInfo companyReq);
    public BaseResultDTO updateCompany(CompanyInfo companyReq);
    public BaseResultDTO deleteCompany(Long id);
}
