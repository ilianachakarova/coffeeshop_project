package com.chakarova.demo.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity{
    private LocalDateTime timeClosed;
    private List<OrderItem> products;
    private User employee;

    public Order() {
    }
    @Column(name = "time_closed", nullable = false)
    public LocalDateTime getTimeClosed() {
        return timeClosed;
    }

    public void setTimeClosed(LocalDateTime timeClosed) {
        this.timeClosed = timeClosed;
    }
    @ManyToMany(targetEntity = OrderItem.class, cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "orders_products",
    joinColumns = @JoinColumn(name = "order_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "product_id",referencedColumnName = "id"))
    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }
    @ManyToOne(targetEntity = User.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }
}
