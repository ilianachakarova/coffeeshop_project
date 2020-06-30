package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.UserRepository;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.service.RoleService;
import com.chakarova.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(s).orElseThrow(()->new UsernameNotFoundException("User not found!"));
    }
}
