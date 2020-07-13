package com.chakarova.demo.model.entity;

import org.springframework.stereotype.Controller;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity{
    private LocalDateTime timeClosed;
    private List<Product> products;
    private BigDecimal totalPrice;
    private User employee;

    public Order() {
    }
    public Order(LocalDateTime timeClosed, List<Product>products, BigDecimal totalPrice, User employee) {
        this.timeClosed = timeClosed;
        this.products = products;
        this.totalPrice = totalPrice;
        this.employee = employee;
    }

    @Column(name = "time_closed", nullable = false)
    public LocalDateTime getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(LocalDateTime timeClosed) {
        this.timeClosed = timeClosed;
    }

    @ManyToMany(targetEntity = Product.class,fetch = FetchType.EAGER)
    @JoinTable(name = "orders_products",
    joinColumns = @JoinColumn(name = "order_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "product_id",referencedColumnName = "id"))
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
    @Column(name = "total_price")
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
