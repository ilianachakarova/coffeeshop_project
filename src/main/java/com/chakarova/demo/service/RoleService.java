package com.chakarova.demo.service;

import com.chakarova.demo.model.entity.Role;

public interface RoleService {
    void seedRolesInDb();
    Role findRoleByName(String name);
}
