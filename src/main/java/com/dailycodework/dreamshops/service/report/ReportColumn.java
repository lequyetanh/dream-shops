package com.dailycodework.dreamshops.service.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportColumn {
    private String header;
    private String field;
    private int width;
}
