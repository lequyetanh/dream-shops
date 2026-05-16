package com.dailycodework.dreamshops.service.statistics;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.repository.statistics.StatisticsRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsService implements IStatisticsService {
    private final StatisticsRepositoryCustom statisticsRepository;

    @Override
    public BaseResultDTO getRevenue(String fromDate, String toDate, String groupBy, Long companyId) {
        var result = statisticsRepository.getRevenue(fromDate, toDate, groupBy, companyId);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO getTopProducts(Long companyId, String fromDate, String toDate, Integer limit) {
        int topLimit = (limit != null && limit > 0) ? limit : 10;
        var result = statisticsRepository.getTopProducts(companyId, fromDate, toDate, topLimit);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO getOrderStatusCount(Long companyId, String fromDate, String toDate) {
        var result = statisticsRepository.getOrderStatusCount(companyId, fromDate, toDate);
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }
}
