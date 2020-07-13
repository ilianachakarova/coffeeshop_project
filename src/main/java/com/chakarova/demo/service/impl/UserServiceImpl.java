package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.UserRepository;
import com.chakarova.demo.model.binding.UpdateUserBindingModel;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.entity.enums.RoleNames;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.model.view.UsersAllViewModel;
import com.chakarova.demo.service.RoleService;
import com.chakarova.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;

        this.encoder = encoder;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {
        User entity = this.modelMapper.map(userServiceModel, User.class);
        if(this.userRepository.count()==0){
            entity.setAuthorities(Set.of(this.roleService.findRoleByName(RoleNames.ROLE_ROOT.name())));
        }else {
            entity.setAuthorities(Set.of(this.roleService.findRoleByName(RoleNames.ROLE_USER.name())));
        }
        entity.setPassword(encoder.encode(userServiceModel.getPassword()));
        return this.modelMapper.map(this.userRepository.save(entity),UserServiceModel.class);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(s).orElseThrow(()->new UsernameNotFoundException("User not found!"));
    }

    @Override
    public List<UsersAllViewModel> findAllUsers() {
        return this.userRepository.findAll().stream()
                .map(u->{
                 UsersAllViewModel model = this.modelMapper.map(u,UsersAllViewModel.class);
                 model.setRoles(u.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet()));
                 return model;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceModel findUserById(Long id) {
        User user = this.userRepository.findById(id).orElse(null);
        return this.modelMapper.map(user, UserServiceModel.class);
    }

    @Override
    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public void updateUser(UpdateUserBindingModel updateUserBindingModel, Long id) {
        User user = this.userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("User not found"));
        user.setSalary(updateUserBindingModel.getSalary());
        user.setBonus(updateUserBindingModel.getBonus());
        user.setAuthorities(Set.of(this.roleService.findRoleByName(updateUserBindingModel.getRole())));
        this.userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String name) {
        return this.userRepository.
                findByUsername(name).orElseThrow(()->new IllegalArgumentException("No such user"));
    }
}
