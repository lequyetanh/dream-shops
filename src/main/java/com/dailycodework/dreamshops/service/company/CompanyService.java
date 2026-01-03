package com.dailycodework.dreamshops.service.company;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.company.CompanyInfo;
import com.dailycodework.dreamshops.dto.company.UserLogin;
import com.dailycodework.dreamshops.repository.company.ICompanyRepository;
import com.dailycodework.dreamshops.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService implements ICompanyService {
    private final ICompanyRepository companyRepository;

    @Override
    public BaseResultDTO getCompanyWithPaging(
            Pageable pageable,
            String keyword
    ){
        return null;
    };
    @Override
    public BaseResultDTO findById(Long id){
        return null;
    };
    @Override
    public BaseResultDTO createCompany(CompanyInfo companyReq){
        return null;
    };
    @Override
    public BaseResultDTO updateCompany(CompanyInfo companyReq){
        return null;
    };
    @Override
    public BaseResultDTO deleteCompany(Long id){
        return null;
    };

    @Override
    public BaseResultDTO login(UserLogin userLogin){
        return null;
    }
}
