package com.dailycodework.dreamshops.service.company;

import com.dailycodework.dreamshops.constant.ConfigConstant;
import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.Config;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.company.CompanyInfo;
import com.dailycodework.dreamshops.payload.dto.company.UserLogin;
import com.dailycodework.dreamshops.entity.Company;
import com.dailycodework.dreamshops.repository.company.ICompanyRepository;
import com.dailycodework.dreamshops.repository.config.IConfigRepository;
import com.dailycodework.dreamshops.service.RedisManagementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService implements ICompanyService {
    private final ICompanyRepository companyRepository;
    private final IConfigRepository configRepository;
    public final RedisManagementService redisManagementService;
    private final ObjectMapper objectMapper;

    @Override
    public BaseResultDTO getCompanyWithPaging(Pageable pageable, String keyword) {
        Page<CompanyInfo> page = companyRepository.getWithPaging(pageable, keyword);
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                page.getContent(),
                (int) page.getTotalElements()
        );
    }
    @Override
    public BaseResultDTO findById(Long id){
        CompanyInfo companyReturn = new CompanyInfo();
        if(redisManagementService.getInHash("company" + id, "name") != null){
            String companyName = redisManagementService.getInHash("company" + id, "name").toString();
            companyReturn.setName(companyName);
            try{
                BaseResultDTO result = new BaseResultDTO(
                        ResultNotify.successGet,
                        true,
                        companyReturn
                );
                return result;
            }catch (Exception e){
                e.printStackTrace();
            }

        }else{
            Optional<Company> company = companyRepository.findById(id);
            if(company.isPresent()){
                BeanUtils.copyProperties(company.get(), companyReturn);
                redisManagementService.putToHash("company" + id, "name", company.get().getName());
                redisManagementService.putToHash("company" + id, "phone", company.get().getPhone());
                redisManagementService.putToHash("company" + id, "extra", company.get().getExtra());
                BaseResultDTO result = new BaseResultDTO(
                        ResultNotify.successGet,
                        true,
                        companyReturn
                );
                return result;
            }
        }
        return null;
    };
    @Override
    public BaseResultDTO createCompany(CompanyInfo companyReq){
        Company company = new Company();
        BeanUtils.copyProperties(companyReq, company);
        company.setExtra(objectMapper.writeValueAsString(companyReq.getExtra()));
        companyRepository.save(company);

        List<Config> defaultConfigs = configRepository.findAllByCompanyIdAndCodes(
                1L,
                Arrays.asList(
                        ConfigConstant.INVOICE_TYPE,
                        ConfigConstant.TAXI_CONFIG,
                        ConfigConstant.TYPE_DISCOUNT,
                        ConfigConstant.VOUCHER_APPLY,
                        ConfigConstant.SEPARATOR,
                        ConfigConstant.CURRENCY_DENOMINATION_CONFIGURATION
                )
        );
        for (Config defaultConfig : defaultConfigs) {
            Config newConfig = new Config();
            newConfig.setCompanyId(company.getId());
            newConfig.setCode(defaultConfig.getCode());
            newConfig.setValue(defaultConfig.getValue());
            newConfig.setDescription(defaultConfig.getDescription());
            configRepository.save(newConfig);
        }

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
    public BaseResultDTO login(UserLogin userLogin) {
        Optional<com.dailycodework.dreamshops.entity.Company> companyOpt = companyRepository.findByTaxCode(userLogin.getUserName());
        if (companyOpt.isEmpty()) {
            return new BaseResultDTO("Không tìm thấy công ty với mã số thuế này", false, null);
        }
        com.dailycodework.dreamshops.entity.Company company = companyOpt.get();
        if (!userLogin.getPassword().equals(company.getPassword())) {
            return new BaseResultDTO("Mật khẩu không đúng", false, null);
        }
        return new BaseResultDTO(ResultNotify.successGet, true, company);
    }
}
