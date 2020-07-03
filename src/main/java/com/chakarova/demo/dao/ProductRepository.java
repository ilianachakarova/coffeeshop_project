package com.chakarova.demo.dao;

import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByName(String name);
    List<Product>findByCategory(Category category);

}
