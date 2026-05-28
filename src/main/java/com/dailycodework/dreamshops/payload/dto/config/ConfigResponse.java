package com.dailycodework.dreamshops.payload.dto.config;

import lombok.Data;

import java.util.List;

@Data
public class ConfigResponse {
    private Long companyId;
    private Integer invoiceType;
    private Integer typeDiscount;
    private Integer voucherApply;
    private Integer businessType;
    private TaxiConfig taxiConfig;
    private Separator separator;
    private CurrencyDenominationConfiguration currencyDenominationConfiguration;

    @Data
    public static class TaxiConfig {
        private TripInfo tripInfo;
        private Integer saleType;
    }

    @Data
    public static class TripInfo {
        private String code;
        private String name;
        private String value;
        private String invoiceCode;
        private Boolean isDisplay;
    }

    @Data
    public static class Separator {
        private String thousand;
        private String decimal;
    }

    @Data
    public static class CurrencyDenominationConfiguration {
        private List<Integer> money;
        private List<Integer> liter;
    }
}
