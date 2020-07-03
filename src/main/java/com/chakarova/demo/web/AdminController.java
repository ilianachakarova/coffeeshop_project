package com.chakarova.demo.web;

import com.chakarova.demo.model.binding.UpdateUserBindingModel;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.model.view.UsersAllViewModel;
import com.chakarova.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @GetMapping("/all-users")
    public ModelAndView showAllEmployees(ModelAndView modelAndView){
        modelAndView.addObject("users",this.userService.findAllUsers());
        modelAndView.setViewName("admin/all-users");
        return modelAndView;
    }

    @GetMapping("/update/user/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView userUpdate(@PathVariable("id")Long id, ModelAndView modelAndView, Model model){
        UserServiceModel userServiceModel =this.userService.findUserById(id);
        UsersAllViewModel user = this.modelMapper.map(userServiceModel,UsersAllViewModel.class);
        user.setRoles(userServiceModel.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet()));
        modelAndView.addObject("user",user);
        if(!model.containsAttribute("updateUserBindingModel")){
            model.addAttribute("updateUserBindingModel", new UpdateUserBindingModel());
        }
        modelAndView.setViewName("admin/employee-update");

        return modelAndView;
    }

    @PostMapping("/update/user/{id}")
    public String confirmUserUpdate(@PathVariable("id") Long id, Model model,
                                    @ModelAttribute("updateUserBindingModel") UpdateUserBindingModel updateUserBindingModel,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("updateUserBindingModel",updateUserBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateUserBindingModel",bindingResult);
            return "redirect:/admin/update/user/{id}";
        }

        this.userService.updateUser(updateUserBindingModel, id);
        return "redirect:/admin/all-users";
    }

    @GetMapping("/user/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String deleteEmployee(@PathVariable("id")Long id){
        this.userService.deleteUserById(id);
        return "redirect:/admin/all-users";
    }
}
