package com.dailycodework.dreamshops.repository.customer;

import com.dailycodework.dreamshops.payload.dto.customer.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {
    Page<CustomerInfo> getCustomerWithPaging(Pageable pageable, String keyword, Long companyId);
}
