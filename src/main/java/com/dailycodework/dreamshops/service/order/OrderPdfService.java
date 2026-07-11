package com.dailycodework.dreamshops.service.order;

import com.dailycodework.dreamshops.payload.dto.order.OrderInfo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class OrderPdfService {
    private static final String[] HEADERS = {
            "Mã ĐH", "Khách hàng", "Ngày đặt", "Giảm giá", "Thuế", "Tổng tiền", "Trạng thái"
    };
    private static final float[] COLUMN_WIDTHS = {70, 60, 90, 70, 60, 80, 60};
    private static final float MARGIN = 40;
    private static final float ROW_HEIGHT = 20;
    private static final float FONT_SIZE = 9;
    private static final float TITLE_FONT_SIZE = 14;

    @Value("${app.export.pdf-font-path}")
    private String fontPath;

    public byte[] exportOrders(List<OrderInfo> orders) throws IOException {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDFont font = loadFont(document);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            float pageHeight = page.getMediaBox().getHeight();
            float y = pageHeight - MARGIN;

            contentStream.beginText();
            contentStream.setFont(font, TITLE_FONT_SIZE);
            contentStream.newLineAtOffset(MARGIN, y);
            contentStream.showText("DANH SÁCH ĐƠN HÀNG");
            contentStream.endText();
            y -= ROW_HEIGHT * 1.5f;

            y = drawRow(contentStream, font, HEADERS, MARGIN, y);

            for (OrderInfo order : orders) {
                if (y < MARGIN + ROW_HEIGHT) {
                    contentStream.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    y = pageHeight - MARGIN;
                    y = drawRow(contentStream, font, HEADERS, MARGIN, y);
                }
                String[] values = {
                        nullToEmpty(order.getCode()),
                        order.getCustomerId() != null ? String.valueOf(order.getCustomerId()) : "",
                        nullToEmpty(order.getOrderDate()),
                        order.getDiscountAmount() != null ? order.getDiscountAmount().toPlainString() : "0",
                        order.getVatAmount() != null ? order.getVatAmount().toPlainString() : "0",
                        order.getTotalAmount() != null ? order.getTotalAmount().toPlainString() : "0",
                        order.getStatus() != null ? String.valueOf(order.getStatus()) : ""
                };
                y = drawRow(contentStream, font, values, MARGIN, y);
            }

            contentStream.close();
            document.save(out);
            return out.toByteArray();
        }
    }

    private float drawRow(PDPageContentStream contentStream, PDFont font, String[] values, float startX, float y) throws IOException {
        float x = startX;
        for (int i = 0; i < values.length; i++) {
            contentStream.beginText();
            contentStream.setFont(font, FONT_SIZE);
            contentStream.newLineAtOffset(x, y);
            contentStream.showText(truncate(values[i], COLUMN_WIDTHS[i], font));
            contentStream.endText();
            x += COLUMN_WIDTHS[i];
        }
        return y - ROW_HEIGHT;
    }

    private String truncate(String text, float maxWidth, PDFont font) throws IOException {
        if (text == null) return "";
        String result = text;
        while (!result.isEmpty() && font.getStringWidth(result) / 1000 * FONT_SIZE > maxWidth - 4) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private PDFont loadFont(PDDocument document) throws IOException {
        File file = new File(fontPath);
        if (!file.exists()) {
            throw new IOException(
                    "Không tìm thấy font tại: " + fontPath +
                    ". Cấu hình lại app.export.pdf-font-path trỏ tới một font Unicode hỗ trợ tiếng Việt (vd: Arial, Times New Roman, Noto Sans)."
            );
        }
        return PDType0Font.load(document, file);
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
