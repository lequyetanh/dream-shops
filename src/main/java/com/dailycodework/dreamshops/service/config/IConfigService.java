package com.dailycodework.dreamshops.service.config;

import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.config.ConfigResponse;

public interface IConfigService {
    BaseResultDTO getByCompanyId(Long companyId);
    BaseResultDTO updateConfig(ConfigResponse configResponse);
    BaseResultDTO findById(Long id);
    BaseResultDTO deleteById(Long id);
}
