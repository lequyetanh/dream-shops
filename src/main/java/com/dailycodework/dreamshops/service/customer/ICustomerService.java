package com.dailycodework.dreamshops.service.customer;

import com.dailycodework.dreamshops.dto.BaseResultDTO;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    public BaseResultDTO getCustomerWithPaging(
            Pageable pageable,
            String keyword
    );
    public BaseResultDTO findById(Long id);
    public BaseResultDTO createCustomer ();
    public BaseResultDTO updateCustomer ();
    public BaseResultDTO deleteCustomer (Long id);
}
