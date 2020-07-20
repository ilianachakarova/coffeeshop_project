package com.chakarova.demo.model.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DailyReportServiceModel extends BaseServiceModel{
    private BigDecimal revenue;
    private int ordersTotal;
    private LocalDate date;

    public DailyReportServiceModel() {
    }

    public DailyReportServiceModel(BigDecimal revenue, int ordersTotal, LocalDate date) {
        this.revenue = revenue;
        this.ordersTotal = ordersTotal;
        this.date = date;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public int getOrdersTotal() {
        return ordersTotal;
    }

    public void setOrdersTotal(int ordersTotal) {
        this.ordersTotal = ordersTotal;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
