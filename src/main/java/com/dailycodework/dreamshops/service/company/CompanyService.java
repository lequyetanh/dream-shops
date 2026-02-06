package com.dailycodework.dreamshops.service.company;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.company.CompanyInfo;
import com.dailycodework.dreamshops.dto.company.UserLogin;
import com.dailycodework.dreamshops.entity.Company;
import com.dailycodework.dreamshops.repository.company.ICompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<Company> company = companyRepository.findById(id);
        if(company.isPresent()){
            BaseResultDTO result = new BaseResultDTO(
                    ResultNotify.successGet,
                    true,
                    company.get()
            );
            return result;
        }
        return null;
    };
    @Override
    public BaseResultDTO createCompany(CompanyInfo companyReq){
        Company company = new Company();
        BeanUtils.copyProperties(companyReq, company);
        companyRepository.save(company);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                company
        );
    };
    @Override
    public BaseResultDTO updateCompany(CompanyInfo companyReq){
        Optional<Company> companyOpt = companyRepository.findById(companyReq.getId());
        if(companyOpt.isPresent()){
            Company company = companyOpt.get();
            BeanUtils.copyProperties(companyReq, company);
            companyRepository.save(company);
            return new BaseResultDTO(
                    ResultNotify.successUpdate,
                    true,
                    company
            );
        }
        return null;
    };
    @Override
    public BaseResultDTO deleteCompany(Long id){
        Optional<Company> companyOpt = companyRepository.findById(id);
        if(companyOpt.isPresent()){
            companyRepository.deleteById(id);
            return new BaseResultDTO(
                    ResultNotify.successDelete,
                    true,
                    null
            );
        }
        return null;
    };

    @Override
    public BaseResultDTO login(UserLogin userLogin){
        return null;
    }
}
