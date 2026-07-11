package com.dailycodework.dreamshops.service.report;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ReportExcelExportService {

    public byte[] export(String title, String subtitle, List<ReportColumn> columns, List<Map<String, Object>> rows) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName(title));

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            int rowIdx = 0;
            Row titleRow = sheet.createRow(rowIdx++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(title);
            titleCell.setCellStyle(titleStyle);

            if (subtitle != null && !subtitle.isBlank()) {
                Row subtitleRow = sheet.createRow(rowIdx++);
                subtitleRow.createCell(0).setCellValue(subtitle);
            }
            rowIdx++;

            Row headerRow = sheet.createRow(rowIdx++);
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i).getHeader());
                cell.setCellStyle(headerStyle);
            }

            for (Map<String, Object> row : rows) {
                Row excelRow = sheet.createRow(rowIdx++);
                for (int i = 0; i < columns.size(); i++) {
                    Cell cell = excelRow.createCell(i);
                    setCellValue(cell, row.get(columns.get(i).getField()));
                }
            }

            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private String sheetName(String title) {
        String sanitized = title.replaceAll("[\\\\/*\\[\\]:?]", " ").trim();
        return sanitized.length() > 31 ? sanitized.substring(0, 31) : sanitized;
    }
}
