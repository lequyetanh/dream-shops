package com.dailycodework.dreamshops.service.statistics;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.OrderStatusCountDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.RevenueStatDTO;
import com.dailycodework.dreamshops.payload.dto.statistics.TopProductDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService implements IStatisticsService {
    private final EntityManager entityManager;

    @Override
    public BaseResultDTO getRevenue(String fromDate, String toDate, String groupBy, Long companyId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" where 1=1 ");

        if (companyId != null) {
            where.append(" and o.company_id = :companyId ");
            params.put("companyId", companyId);
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            where.append(" and o.order_date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            where.append(" and o.order_date <= :toDate ");
            params.put("toDate", toDate);
        }

        // SQL Server: group by day hoặc month
        String dateExpr = "month".equalsIgnoreCase(groupBy)
                ? "FORMAT(o.order_date, 'yyyy-MM')"
                : "CONVERT(varchar, CAST(o.order_date AS DATE), 23)";

        String sql = "select " + dateExpr + " as dateLabel, " +
                "ISNULL(SUM(o.total_amount), 0) as revenue, " +
                "COUNT(*) as orderCount " +
                "from orders o " + where +
                " group by " + dateExpr +
                " order by " + dateExpr;

        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);

        List<Object[]> rows = query.getResultList();
        List<RevenueStatDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new RevenueStatDTO(
                    (String) row[0],
                    row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO,
                    ((Number) row[2]).intValue()
            ));
        }
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO getTopProducts(Long companyId, String fromDate, String toDate, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" where 1=1 ");

        if (companyId != null) {
            where.append(" and o.company_id = :companyId ");
            params.put("companyId", companyId);
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            where.append(" and o.order_date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            where.append(" and o.order_date <= :toDate ");
            params.put("toDate", toDate);
        }

        int topLimit = (limit != null && limit > 0) ? limit : 10;

        String sql = "select top " + topLimit + " " +
                "op.product_id, op.product_name, " +
                "SUM(op.quantity) as totalQuantity, " +
                "SUM(op.total_price) as totalRevenue " +
                "from order_product op " +
                "join orders o on op.order_id = o.id " + where +
                " group by op.product_id, op.product_name " +
                " order by totalRevenue desc";

        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);

        List<Object[]> rows = query.getResultList();
        List<TopProductDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new TopProductDTO(
                    row[0] != null ? ((Number) row[0]).longValue() : null,
                    (String) row[1],
                    row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO,
                    row[3] != null ? new BigDecimal(row[3].toString()) : BigDecimal.ZERO
            ));
        }
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }

    @Override
    public BaseResultDTO getOrderStatusCount(Long companyId, String fromDate, String toDate) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" where 1=1 ");

        if (companyId != null) {
            where.append(" and o.company_id = :companyId ");
            params.put("companyId", companyId);
        }
        if (fromDate != null && !fromDate.isEmpty()) {
            where.append(" and o.order_date >= :fromDate ");
            params.put("fromDate", fromDate);
        }
        if (toDate != null && !toDate.isEmpty()) {
            where.append(" and o.order_date <= :toDate ");
            params.put("toDate", toDate);
        }

        String sql = "select o.status, COUNT(*) as orderCount " +
                "from orders o " + where +
                " group by o.status order by o.status";

        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);

        List<Object[]> rows = query.getResultList();
        List<OrderStatusCountDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new OrderStatusCountDTO(
                    row[0] != null ? ((Number) row[0]).intValue() : null,
                    ((Number) row[1]).intValue()
            ));
        }
        return new BaseResultDTO(ResultNotify.successGet, true, result, result.size());
    }
}
