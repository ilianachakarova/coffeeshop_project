package com.chakarova.demo.web.scheduler;

import com.chakarova.demo.model.service.DailyReportServiceModel;
import com.chakarova.demo.service.DailyReportService;
import com.chakarova.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DailyReportScheduler {
    private final OrderService orderService;
    private final DailyReportService dailyReportService;
    @Autowired
    public DailyReportScheduler(OrderService orderService, DailyReportService dailyReportService) {
        this.orderService = orderService;
        this.dailyReportService = dailyReportService;
    }
    @Scheduled(cron = "0 0 0 * * ?") //every day
    // @Scheduled(cron = "0 * * ? * *") //test
    public void saveDailyReport(){
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        BigDecimal revenue = orderService.findTotalRevenueForPeriod(start,end);

        int ordersTotal = orderService.findOrdersInTimeRange(start,end).size();
        dailyReportService.saveDailyReport(new DailyReportServiceModel(revenue,ordersTotal,LocalDate.now()));
    }
}
