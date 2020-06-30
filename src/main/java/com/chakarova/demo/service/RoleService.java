package com.chakarova.demo.service;

import com.chakarova.demo.model.service.UserServiceModel;

public interface RoleService {
    void seedRolesInDb();
    void assignUserRoles(UserServiceModel userServiceModel);
}
