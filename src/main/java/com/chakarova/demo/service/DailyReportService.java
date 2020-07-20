package com.chakarova.demo.service;

import com.chakarova.demo.model.service.DailyReportServiceModel;

import java.time.LocalDate;
import java.util.List;

public interface DailyReportService {
    DailyReportServiceModel saveDailyReport(DailyReportServiceModel dailyReportServiceModel);
    DailyReportServiceModel findReportByDate(LocalDate date);
    List<DailyReportServiceModel> findReportsFromDate(LocalDate date);
    List<DailyReportServiceModel> findAllReports();
}
