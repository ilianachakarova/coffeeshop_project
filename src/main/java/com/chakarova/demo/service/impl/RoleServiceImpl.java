package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.RoleRepository;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void seedRolesInDb() {
        if(roleRepository.count()==0){
            this.roleRepository.save(new Role("ROLE_ADMIN"));
            this.roleRepository.save(new Role("ROLE_EMPLOYEE"));
        }
    }

    @Override
    public void assignUserRoles(UserServiceModel userServiceModel) {

    }
}
