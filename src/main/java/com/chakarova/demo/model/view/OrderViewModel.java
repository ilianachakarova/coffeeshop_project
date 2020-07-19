package com.chakarova.demo.model.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderViewModel {
    private Long id;
    private List<String>products;
    private LocalDateTime timeClosed;
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

    public LocalDateTime getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(LocalDateTime timeClosed) {
        this.timeClosed = timeClosed;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
