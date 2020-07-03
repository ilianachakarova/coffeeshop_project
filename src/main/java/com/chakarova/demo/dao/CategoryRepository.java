package com.chakarova.demo.dao;

import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategory(CategoryNames category);
}
