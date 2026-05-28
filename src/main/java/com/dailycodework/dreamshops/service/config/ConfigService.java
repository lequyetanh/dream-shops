package com.dailycodework.dreamshops.service.config;

import com.dailycodework.dreamshops.constant.ConfigConstant;
import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.Config;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.config.ConfigResponse;
import com.dailycodework.dreamshops.repository.config.IConfigRepository;
import com.dailycodework.dreamshops.util.Common;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ConfigService implements IConfigService {

    private final IConfigRepository configRepository;

    public ConfigService(IConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public BaseResultDTO getByCompanyId(Long companyId) {
        List<String> codes = Arrays.asList(
                ConfigConstant.INVOICE_TYPE,
                ConfigConstant.TAXI_CONFIG,
                ConfigConstant.TYPE_DISCOUNT,
                ConfigConstant.VOUCHER_APPLY,
                ConfigConstant.SEPARATOR,
                ConfigConstant.CURRENCY_DENOMINATION_CONFIGURATION
        );
        List<Config> configs = configRepository.findAllByCompanyIdAndCodes(companyId, codes);
        ConfigResponse response = convertConfigToConfigResponse(configs);
        response.setCompanyId(companyId);
        return new BaseResultDTO(ResultNotify.successGet, true, response);
    }

    @Override
    public BaseResultDTO updateConfig(ConfigResponse configResponse) {
        List<Config> configs = convertConfigResponseToConfig(configResponse);
        for (Config config : configs) {
            Optional<Config> existing = configRepository.findByCompanyIdAndCode(
                    config.getCompanyId(), config.getCode());
            if (existing.isPresent()) {
                existing.get().setValue(config.getValue());
                configRepository.save(existing.get());
            } else {
                configRepository.save(config);
            }
        }
        return new BaseResultDTO(ResultNotify.successUpdate, true, null);
    }

    @Override
    public BaseResultDTO findById(Long id) {
        Optional<Config> config = configRepository.findById(id);
        if (config.isEmpty()) {
            throw new RuntimeException(ResultNotify.notFound);
        }
        return new BaseResultDTO(ResultNotify.successGet, true, config.get());
    }

    @Override
    public BaseResultDTO deleteById(Long id) {
        if (!configRepository.existsById(id)) {
            throw new RuntimeException(ResultNotify.notFound);
        }
        configRepository.deleteById(id);
        return new BaseResultDTO(ResultNotify.successDelete, true, null);
    }

    private List<Config> convertConfigResponseToConfig(ConfigResponse configResponse) {
        List<Config> configs = new ArrayList<>();

        if (configResponse.getInvoiceType() != null) {
            Config c = new Config();
            c.setCompanyId(configResponse.getCompanyId());
            c.setCode(ConfigConstant.INVOICE_TYPE);
            c.setValue(String.valueOf(configResponse.getInvoiceType()));
            configs.add(c);
        }
        if (configResponse.getTaxiConfig() != null) {
            Config c = new Config();
            c.setCompanyId(configResponse.getCompanyId());
            c.setCode(ConfigConstant.TAXI_CONFIG);
            c.setValue(Common.toJsonString(configResponse.getTaxiConfig()));
            configs.add(c);
        }
        if (configResponse.getTypeDiscount() != null) {
            Config c = new Config();
            c.setCompanyId(configResponse.getCompanyId());
            c.setCode(ConfigConstant.TYPE_DISCOUNT);
            c.setValue(String.valueOf(configResponse.getTypeDiscount()));
            configs.add(c);
        }
        if (configResponse.getVoucherApply() != null) {
            Config c = new Config();
            c.setCompanyId(configResponse.getCompanyId());
            c.setCode(ConfigConstant.VOUCHER_APPLY);
            c.setValue(String.valueOf(configResponse.getVoucherApply()));
            configs.add(c);
        }
        if (configResponse.getSeparator() != null) {
            Config c = new Config();
            c.setCompanyId(configResponse.getCompanyId());
            c.setCode(ConfigConstant.SEPARATOR);
            c.setValue(Common.toJsonString(configResponse.getSeparator()));
            configs.add(c);
        }
        if (configResponse.getCurrencyDenominationConfiguration() != null) {
            Config c = new Config();
            c.setCompanyId(configResponse.getCompanyId());
            c.setCode(ConfigConstant.CURRENCY_DENOMINATION_CONFIGURATION);
            c.setValue(Common.toJsonString(configResponse.getCurrencyDenominationConfiguration()));
            configs.add(c);
        }

        return configs;
    }

    private ConfigResponse convertConfigToConfigResponse(List<Config> config) {
        ConfigResponse configResponse = new ConfigResponse();
        for (Config configItem : config) {
            if (configItem.getCode().equals(ConfigConstant.INVOICE_TYPE)) {
                configResponse.setInvoiceType(Integer.valueOf(configItem.getValue()));
            }
            if (configItem.getCode().equals(ConfigConstant.TAXI_CONFIG)) {
                configResponse.setTaxiConfig(Common.fromJsonString(configItem.getValue(), ConfigResponse.TaxiConfig.class));
            }
            if (configItem.getCode().equals(ConfigConstant.TYPE_DISCOUNT)) {
                configResponse.setTypeDiscount(Integer.valueOf(configItem.getValue()));
            }
            if (configItem.getCode().equals(ConfigConstant.VOUCHER_APPLY)) {
                configResponse.setVoucherApply(Integer.valueOf(configItem.getValue()));
            }
            if (configItem.getCode().equals(ConfigConstant.SEPARATOR)) {
                configResponse.setSeparator(Common.fromJsonString(configItem.getValue(), ConfigResponse.Separator.class));
            }
            if (configItem.getCode().equals(ConfigConstant.CURRENCY_DENOMINATION_CONFIGURATION)) {
                configResponse.setCurrencyDenominationConfiguration(Common.fromJsonString(configItem.getValue(), ConfigResponse.CurrencyDenominationConfiguration.class));
            }
        }
        return configResponse;
    }
}
