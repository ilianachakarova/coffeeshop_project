package com.chakarova.demo.init;

import com.chakarova.demo.service.CategoryService;
import com.chakarova.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
    private final RoleService roleService;
    private final CategoryService categoryService;

    @Autowired
    public DataInit(RoleService roleService, CategoryService categoryService) {
        this.roleService = roleService;
        this.categoryService = categoryService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.roleService.seedRolesInDb();
        this.categoryService.seedCategoriesInDb();
    }
}
