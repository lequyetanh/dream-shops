package com.dailycodework.dreamshops.repository.company;

import com.dailycodework.dreamshops.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<Company> getWithPaging(Pageable pageable, String keyword);
}
