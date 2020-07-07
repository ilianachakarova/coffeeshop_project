package com.chakarova.demo.model.service;

import com.chakarova.demo.model.entity.OrderItem;
import com.chakarova.demo.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceModel extends BaseServiceModel{
    private LocalDateTime timeClosed;
    private List<OrderItem> products;
    private User employee;

    public OrderServiceModel() {
    }

    public LocalDateTime getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(LocalDateTime timeClosed) {
        this.timeClosed = timeClosed;
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}
