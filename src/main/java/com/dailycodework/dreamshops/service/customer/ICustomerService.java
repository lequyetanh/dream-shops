package com.dailycodework.dreamshops.service.customer;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.customer.CustomerInfo;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    BaseResultDTO getCustomerWithPaging(Pageable pageable, String keyword, Long companyId);
    BaseResultDTO findById(Long id);
    BaseResultDTO createCustomer(CustomerInfo customerReq);
    BaseResultDTO updateCustomer(CustomerInfo customerReq);
    BaseResultDTO deleteCustomer(Long id);
    BaseResultDTO getOrderHistory(Long customerId);
    BaseResultDTO getSpendingSummary(Long customerId);
}
