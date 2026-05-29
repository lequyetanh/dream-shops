package com.dailycodework.dreamshops.repository.report;

import com.dailycodework.dreamshops.payload.dto.report.InventoryReportDTO;
import com.dailycodework.dreamshops.payload.dto.report.RevenueByCategoryDTO;
import com.dailycodework.dreamshops.payload.dto.report.RevenueComparisonDTO;
import com.dailycodework.dreamshops.payload.dto.report.TopCustomerDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {
    private final EntityManager entityManager;

    @Override
    public List<InventoryReportDTO> getInventoryReport(Long companyId, Long categoryId) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" where 1=1 ");

        if (companyId != null) {
            where.append(" and p.company_id = :companyId ");
            params.put("companyId", companyId);
        }
        if (categoryId != null) {
            where.append(" and c.id = :categoryId ");
            params.put("categoryId", categoryId);
        }

        // STRING_AGG yêu cầu SQL Server 2017+
        String sql = "select p.id, p.name, p.barcode, " +
                "p.stock_quantity, p.in_price, " +
                "ISNULL(p.stock_quantity * p.in_price, 0) as stockValue, " +
                "STRING_AGG(c.name, ', ') as categoryNames " +
                "from product p " +
                "left join product_category pc on p.id = pc.product_id " +
                "left join category c on pc.category_id = c.id " +
                where +
                " group by p.id, p.name, p.barcode, p.stock_quantity, p.in_price " +
                " order by p.stock_quantity asc";

        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);

        List<Object[]> rows = query.getResultList();
        List<InventoryReportDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new InventoryReportDTO(
                    row[0] != null ? ((Number) row[0]).longValue() : null,
                    (String) row[1],
                    (String) row[2],
                    row[3] != null ? ((Number) row[3]).intValue() : 0,
                    row[4] != null ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO,
                    row[5] != null ? new BigDecimal(row[5].toString()) : BigDecimal.ZERO,
                    (String) row[6]
            ));
        }
        return result;
    }

    @Override
    public List<RevenueByCategoryDTO> getRevenueByCategoryReport(Long companyId, String fromDate, String toDate) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder where = new StringBuilder(" where 1=1 ");

        if (companyId != null) {
            where.append(" and c.company_id = :companyId ");
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

        String sql = "select c.id, c.name, " +
                "COUNT(distinct o.id) as orderCount, " +
                "ISNULL(SUM(op.total_price), 0) as totalRevenue " +
                "from category c " +
                "left join product_category pc on c.id = pc.category_id " +
                "left join product p on pc.product_id = p.id " +
                "left join order_product op on p.id = op.product_id " +
                "left join orders o on op.order_id = o.id " +
                where +
                " group by c.id, c.name " +
                " order by totalRevenue desc";

        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);

        List<Object[]> rows = query.getResultList();
        List<RevenueByCategoryDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new RevenueByCategoryDTO(
                    row[0] != null ? ((Number) row[0]).longValue() : null,
                    (String) row[1],
                    ((Number) row[2]).intValue(),
                    row[3] != null ? new BigDecimal(row[3].toString()) : BigDecimal.ZERO
            ));
        }
        return result;
    }

    @Override
    public List<TopCustomerDTO> getTopCustomersReport(Long companyId, String fromDate, String toDate, int limit) {
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

        String sql = "select cu.id, cu.name, cu.code, " +
                "COUNT(*) as orderCount, " +
                "SUM(o.total_amount) as totalSpending, " +
                "AVG(o.total_amount) as avgOrderValue " +
                "from customer cu " +
                "join orders o on cu.id = o.customer_id " +
                where +
                " group by cu.id, cu.name, cu.code " +
                " order by totalSpending desc";

        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);
        query.setMaxResults(limit);

        List<Object[]> rows = query.getResultList();
        List<TopCustomerDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new TopCustomerDTO(
                    row[0] != null ? ((Number) row[0]).longValue() : null,
                    (String) row[1],
                    (String) row[2],
                    ((Number) row[3]).intValue(),
                    row[4] != null ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO,
                    row[5] != null ? new BigDecimal(row[5].toString()) : BigDecimal.ZERO
            ));
        }
        return result;
    }

    @Override
    public List<RevenueComparisonDTO.PeriodPoint> getRevenuePeriod(Long companyId, String fromDate, String toDate, String groupBy) {
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
        List<RevenueComparisonDTO.PeriodPoint> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new RevenueComparisonDTO.PeriodPoint(
                    (String) row[0],
                    row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO,
                    ((Number) row[2]).intValue()
            ));
        }
        return result;
    }
}
