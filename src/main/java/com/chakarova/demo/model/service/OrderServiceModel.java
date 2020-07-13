package com.chakarova.demo.model.service;

import com.chakarova.demo.model.entity.Product;
import com.chakarova.demo.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class OrderServiceModel extends BaseServiceModel{
    private LocalDateTime timeClosed;
    private List<Product> products;
    private User employee;

    public OrderServiceModel() {
    }

    public LocalDateTime getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(LocalDateTime timeClosed) {
        this.timeClosed = timeClosed;
    }


    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
