package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.constant.BaseConstant;
import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
import com.dailycodework.dreamshops.payload.dto.orderProduct.OrderProductReq;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderExcelService {
    private static final String[] EXPORT_HEADERS = {
            "Mã đơn hàng", "Mã khách hàng", "Ngày đặt hàng", "Mô tả", "Số tiền giảm giá",
            "Thuế suất (%)", "Tiền thuế", "Tổng tiền", "Mã công ty", "Trạng thái", "Mã giảm giá"
    };
    private final DataFormatter dataFormatter = new DataFormatter();

    public List<OrderInfo> parseOrders(MultipartFile file) throws IOException {
        Map<String, OrderInfo> orderByCode = new LinkedHashMap<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = WorkbookFactory.create(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                String code = getStringCell(row, 0);
                if (!StringUtils.hasText(code)) continue;

                OrderInfo orderInfo = orderByCode.computeIfAbsent(code, c -> {
                    OrderInfo info = new OrderInfo();
                    info.setCode(c);
                    info.setCustomerId(getLongCell(row, 1));
                    info.setOrderDate(getDateCell(row, 2));
                    info.setDescription(getStringCell(row, 3));
                    info.setVatRate(getIntCell(row, 4));
                    info.setCompanyId(getLongCell(row, 5));
                    info.setVoucherCode(getStringCell(row, 6));
                    info.setProducts(new ArrayList<>());
                    return info;
                });

                OrderProductReq productReq = new OrderProductReq();
                productReq.setProductId(getLongCell(row, 7));
                productReq.setProductName(getStringCell(row, 8));
                BigDecimal price = getDecimalCell(row, 9);
                BigDecimal quantity = getDecimalCell(row, 10);
                productReq.setPrice(price);
                productReq.setQuantity(quantity);
                productReq.setTotalPrice(price != null && quantity != null ? price.multiply(quantity) : null);
                orderInfo.getProducts().add(productReq);
            }
        }

        for (OrderInfo orderInfo : orderByCode.values()) {
            BigDecimal subtotal = orderInfo.getProducts().stream()
                    .map(OrderProductReq::getTotalPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal vatAmount = orderInfo.getVatRate() != null
                    ? subtotal.multiply(BigDecimal.valueOf(orderInfo.getVatRate())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            orderInfo.setVatAmount(vatAmount);
            orderInfo.setDiscountAmount(BigDecimal.ZERO);
            orderInfo.setTotalAmount(subtotal.add(vatAmount));
        }

        return new ArrayList<>(orderByCode.values());
    }

    public byte[] exportOrders(List<OrderInfo> orders) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Orders");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(EXPORT_HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (OrderInfo order : orders) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(nullToEmpty(order.getCode()));
                row.createCell(1).setCellValue(order.getCustomerId() != null ? order.getCustomerId() : 0);
                row.createCell(2).setCellValue(nullToEmpty(order.getOrderDate()));
                row.createCell(3).setCellValue(nullToEmpty(order.getDescription()));
                row.createCell(4).setCellValue(order.getDiscountAmount() != null ? order.getDiscountAmount().doubleValue() : 0);
                row.createCell(5).setCellValue(order.getVatRate() != null ? order.getVatRate() : 0);
                row.createCell(6).setCellValue(order.getVatAmount() != null ? order.getVatAmount().doubleValue() : 0);
                row.createCell(7).setCellValue(order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0);
                row.createCell(8).setCellValue(order.getCompanyId() != null ? order.getCompanyId() : 0);
                row.createCell(9).setCellValue(order.getStatus() != null ? order.getStatus() : 0);
                row.createCell(10).setCellValue(nullToEmpty(order.getVoucherCode()));
            }

            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int c = 0; c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK && !dataFormatter.formatCellValue(cell).isBlank()) {
                return false;
            }
        }
        return true;
    }

    private String getStringCell(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        String value = dataFormatter.formatCellValue(cell).trim();
        return value.isEmpty() ? null : value;
    }

    private String getDateCell(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell != null && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return new SimpleDateFormat(BaseConstant.ZONED_DATE_TIME_FORMAT).format(cell.getDateCellValue());
        }
        return getStringCell(row, idx);
    }

    private Long getLongCell(Row row, int idx) {
        String value = getStringCell(row, idx);
        if (value == null) return null;
        try {
            return (long) Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer getIntCell(Row row, int idx) {
        Long value = getLongCell(row, idx);
        return value != null ? value.intValue() : null;
    }

    private BigDecimal getDecimalCell(Row row, int idx) {
        String value = getStringCell(row, idx);
        if (value == null) return null;
        try {
            return new BigDecimal(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
