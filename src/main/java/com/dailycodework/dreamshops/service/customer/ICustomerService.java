package com.dailycodework.dreamshops.service.customer;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.customer.CustomerInfo;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    public BaseResultDTO getCustomerWithPaging(
            Pageable pageable,
            String keyword
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createCustomer (CustomerInfo customerReq);
    public BaseResultDTO updateCustomer (CustomerInfo customerReq);
    public BaseResultDTO deleteCustomer (Long id);
}
