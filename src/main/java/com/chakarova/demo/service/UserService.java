package com.chakarova.demo.service;

import com.chakarova.demo.model.binding.UpdateUserBindingModel;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.model.view.UsersAllViewModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserServiceModel userServiceModel);
    List<UsersAllViewModel>findAllUsers();
    UserServiceModel findUserById(Long id);
    void deleteUserById(Long id);

    void updateUser(UpdateUserBindingModel updateUserBindingModel, Long id);

    User findUserByUsername(String name);
}
