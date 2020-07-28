package com.chakarova.demo.dao;

import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByName(String name);
    List<Product>findByCategory(Category category);
    List<Product>findAllByCategory(Category category);

    @Transactional
    @Modifying
    @Query("delete from Product p where p.id =:id")
    void deleteProductById(@Param(value="id")Long id);
}
