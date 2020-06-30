package com.chakarova.demo.service;

import com.chakarova.demo.model.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserServiceModel userServiceModel);
}
