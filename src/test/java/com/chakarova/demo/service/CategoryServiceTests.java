package com.chakarova.demo.service;

import com.chakarova.demo.dao.CategoryRepository;
import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.service.impl.CategoryServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryServiceTests {


    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void categoryService_findCategoryByName_ReturnsCorrectValue() {
        CategoryService categoryServiceToTest = new CategoryServiceImpl(categoryRepository);
        Category category = new Category();
        category.setId(1L);
        category.setCategory(CategoryNames.COLD_DRINK);
        categoryRepository.save(category);
        //act
        Category actual = categoryServiceToTest.findCategoryByName(CategoryNames.COLD_DRINK);
        Category expected = this.categoryRepository.findAll().get(0);
        //assert
        Assert.assertEquals(actual.getCategory(),expected.getCategory());
        Assert.assertEquals(actual.getId(),expected.getId());

    }

    @Test
    public void categoryService_seedCategoriesInDb_returnsCorrectCount(){

        CategoryService categoryServiceToTest = new CategoryServiceImpl(categoryRepository);
        categoryServiceToTest.seedCategoriesInDb();
        Assert.assertEquals(4,categoryRepository.count());
    }
}
