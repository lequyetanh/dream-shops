package com.dailycodework.dreamshops.repository.company;

import com.dailycodework.dreamshops.payload.dto.company.CompanyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyRepositoryCustom {
    Page<CompanyInfo> getWithPaging(Pageable pageable, String keyword);
}
