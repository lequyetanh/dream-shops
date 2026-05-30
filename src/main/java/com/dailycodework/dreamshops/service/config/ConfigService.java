package com.dailycodework.dreamshops.service.config;

import com.dailycodework.dreamshops.constant.ConfigConstant;
import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.Config;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.config.ConfigResponse;
import com.dailycodework.dreamshops.repository.config.IConfigRepository;
import com.dailycodework.dreamshops.util.Common;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        List<String> codes = Arrays.asList(
                ConfigConstant.INVOICE_TYPE,
                ConfigConstant.TAXI_CONFIG,
                ConfigConstant.TYPE_DISCOUNT,
                ConfigConstant.VOUCHER_APPLY,
                ConfigConstant.SEPARATOR,
                ConfigConstant.CURRENCY_DENOMINATION_CONFIGURATION
        );
        return codes.stream()
                .map(code -> buildConfigFromCode(configResponse, code))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Config buildConfigFromCode(ConfigResponse configResponse, String code) {
        Object value = switch (code) {
            case ConfigConstant.INVOICE_TYPE -> configResponse.getInvoiceType();
            case ConfigConstant.TAXI_CONFIG -> configResponse.getTaxiConfig();
            case ConfigConstant.TYPE_DISCOUNT -> configResponse.getTypeDiscount();
            case ConfigConstant.VOUCHER_APPLY -> configResponse.getVoucherApply();
            case ConfigConstant.SEPARATOR -> configResponse.getSeparator();
            case ConfigConstant.CURRENCY_DENOMINATION_CONFIGURATION -> configResponse.getCurrencyDenominationConfiguration();
            default -> null;
        };
        if (value == null) return null;

        Config c = new Config();
        c.setCompanyId(configResponse.getCompanyId());
        c.setCode(code);
        c.setValue(value instanceof Number ? String.valueOf(value) : Common.toJsonString(value));
        return c;
    }

    // Pattern Strategy Map
    private ConfigResponse convertConfigToConfigResponse(List<Config> configs) {
        ConfigResponse configResponse = new ConfigResponse();
        Map<String, Consumer<String>> handlers = Map.of(
                ConfigConstant.INVOICE_TYPE, v -> configResponse.setInvoiceType(Integer.valueOf(v)),
                ConfigConstant.TAXI_CONFIG, v -> configResponse.setTaxiConfig(Common.fromJsonString(v, ConfigResponse.TaxiConfig.class)),
                ConfigConstant.TYPE_DISCOUNT, v -> configResponse.setTypeDiscount(Integer.valueOf(v)),
                ConfigConstant.VOUCHER_APPLY, v -> configResponse.setVoucherApply(Integer.valueOf(v)),
                ConfigConstant.SEPARATOR, v -> configResponse.setSeparator(Common.fromJsonString(v, ConfigResponse.Separator.class)),
                ConfigConstant.CURRENCY_DENOMINATION_CONFIGURATION, v -> configResponse.setCurrencyDenominationConfiguration(Common.fromJsonString(v, ConfigResponse.CurrencyDenominationConfiguration.class))
        );
        configs.forEach(c -> Optional.ofNullable(handlers.get(c.getCode())).ifPresent(h -> h.accept(c.getValue())));
        return configResponse;
    }
}
