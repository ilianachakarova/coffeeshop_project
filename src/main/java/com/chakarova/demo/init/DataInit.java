package com.chakarova.demo.init;

import com.chakarova.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {
    private final RoleService roleService;

    @Autowired
    public DataInit(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.roleService.seedRolesInDb();
    }
}
