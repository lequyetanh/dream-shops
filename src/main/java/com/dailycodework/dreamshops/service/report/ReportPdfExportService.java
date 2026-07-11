package com.dailycodework.dreamshops.service.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportPdfExportService {
    private static final String FONT_NAME = "DejaVu Sans";
    private static final int PAGE_WIDTH = 842;
    private static final int PAGE_HEIGHT = 595;
    private static final int MARGIN = 30;

    public byte[] export(String title, String subtitle, List<ReportColumn> columns, List<Map<String, Object>> rows) throws JRException {
        JasperDesign design = buildDesign(title, subtitle, columns);
        JasperReport report = JasperCompileManager.compileReport(design);

        Collection<Map<String, ?>> dataRows = new ArrayList<>(rows);
        JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dataRows);
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<>(), dataSource);

        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();
        return out.toByteArray();
    }

    private JasperDesign buildDesign(String title, String subtitle, List<ReportColumn> columns) throws JRException {
        JasperDesign design = new JasperDesign();
        design.setName("dynamicReport");
        design.setLanguage("java");
        design.setPageWidth(PAGE_WIDTH);
        design.setPageHeight(PAGE_HEIGHT);
        design.setOrientation(OrientationEnum.LANDSCAPE);
        design.setColumnWidth(PAGE_WIDTH - MARGIN * 2);
        design.setLeftMargin(MARGIN);
        design.setRightMargin(MARGIN);
        design.setTopMargin(MARGIN);
        design.setBottomMargin(MARGIN);

        JRDesignStyle baseStyle = new JRDesignStyle();
        baseStyle.setName("baseStyle");
        baseStyle.setDefault(true);
        baseStyle.setFontName(FONT_NAME);
        design.addStyle(baseStyle);

        for (ReportColumn column : columns) {
            JRDesignField field = new JRDesignField();
            field.setName(column.getField());
            field.setValueClass(Object.class);
            design.addField(field);
        }

        int titleHeight = subtitle != null && !subtitle.isBlank() ? 60 : 35;
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(titleHeight);

        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setText(title);
        titleText.setX(0);
        titleText.setY(0);
        titleText.setWidth(design.getColumnWidth());
        titleText.setHeight(25);
        titleText.setFontSize(16f);
        titleText.setBold(true);
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setStyle(baseStyle);
        titleBand.addElement(titleText);

        if (subtitle != null && !subtitle.isBlank()) {
            JRDesignStaticText subtitleText = new JRDesignStaticText();
            subtitleText.setText(subtitle);
            subtitleText.setX(0);
            subtitleText.setY(28);
            subtitleText.setWidth(design.getColumnWidth());
            subtitleText.setHeight(20);
            subtitleText.setFontSize(10f);
            subtitleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            subtitleText.setStyle(baseStyle);
            titleBand.addElement(subtitleText);
        }
        design.setTitle(titleBand);

        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(20);
        int x = 0;
        for (ReportColumn column : columns) {
            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setText(column.getHeader());
            headerText.setX(x);
            headerText.setY(0);
            headerText.setWidth(column.getWidth());
            headerText.setHeight(20);
            headerText.setBold(true);
            headerText.setStyle(baseStyle);
            headerText.getLineBox().getBottomPen().setLineWidth(0.5f);
            columnHeaderBand.addElement(headerText);
            x += column.getWidth();
        }
        design.setColumnHeader(columnHeaderBand);

        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(18);
        x = 0;
        for (ReportColumn column : columns) {
            JRDesignTextField textField = new JRDesignTextField();
            JRDesignExpression expression = new JRDesignExpression();
            expression.setText("$F{" + column.getField() + "} == null ? \"\" : $F{" + column.getField() + "}.toString()");
            textField.setExpression(expression);
            textField.setX(x);
            textField.setY(0);
            textField.setWidth(column.getWidth());
            textField.setHeight(18);
            textField.setStyle(baseStyle);
            textField.getLineBox().getBottomPen().setLineWidth(0.25f);
            detailBand.addElement(textField);
            x += column.getWidth();
        }
        ((JRDesignSection) design.getDetailSection()).addBand(detailBand);

        return design;
    }
}
