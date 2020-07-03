package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.CategoryRepository;
import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategoriesInDb() {
        if(categoryRepository.count()==0){

            Arrays.stream(CategoryNames.values()).forEach(categoryName -> {
                this.categoryRepository.save(new Category(categoryName));
            });
        }
    }

    @Override
    public Category findCategoryByName(CategoryNames category) {
        return this.categoryRepository.findByCategory(category);
    }
}
