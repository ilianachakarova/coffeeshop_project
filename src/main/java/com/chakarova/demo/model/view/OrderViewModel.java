package com.chakarova.demo.model.view;

import java.math.BigDecimal;
import java.util.List;

public class OrderViewModel {
    private Long id;
    private List<ProductDetailsViewModel>products;
    private String timeClosed;
    private String employee;
    private BigDecimal total;

    public OrderViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductDetailsViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetailsViewModel> products) {
        this.products = products;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(String timeClosed) {
        this.timeClosed = timeClosed;
    }
}
