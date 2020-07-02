package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.RoleRepository;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.entity.enums.RoleNames;
import com.chakarova.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
            Arrays.stream(RoleNames.values()).forEach(role->{
                this.roleRepository.save(new Role(role.name()));
            });
        }
    }

    @Override
    public Role findRoleByName(String name) {
        return this.roleRepository.findByAuthority(name).orElse(null);
    }
}
