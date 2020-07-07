package com.chakarova.demo.model.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity{
    private Product product;
    private BigDecimal price;

    public OrderItem() {
    }
    @ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "product_id",
    referencedColumnName = "id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    @Column(name = "price", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
