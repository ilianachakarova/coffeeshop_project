package com.chakarova.demo.model.entity;

import com.chakarova.demo.model.entity.enums.CategoryNames;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity{
    private CategoryNames category;
    private Set<Product>products;

    public Category() {
    }

    public Category(CategoryNames category) {
        this.category = category;
    }

    public CategoryNames getCategory() {
        return category;
    }
    @Column(name = "category", nullable = false)
    public void setCategory(CategoryNames category) {
        this.category = category;
    }
    @OneToMany(mappedBy = "category",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
