package com.chakarova.demo.service;

import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.enums.CategoryNames;

public interface CategoryService {
    void seedCategoriesInDb();
    Category findCategoryByName(CategoryNames category);
}
