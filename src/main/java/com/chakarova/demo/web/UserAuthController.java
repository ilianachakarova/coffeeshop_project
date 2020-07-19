package com.chakarova.demo.web;

import com.chakarova.demo.model.binding.UserAddBindingModel;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.model.view.UsersAllViewModel;
import com.chakarova.demo.service.RosterService;
import com.chakarova.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserAuthController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RosterService rosterService;
    @Autowired
    public UserAuthController(ModelMapper modelMapper, UserService userService, RosterService rosterService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.rosterService = rosterService;
    }

    @GetMapping("/register")
    public String register(Model model){
        if(!model.containsAttribute("userAddBindingModel")){
            model.addAttribute("userAddBindingModel", new UserAddBindingModel());
        }
        return "register";
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView){
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @PostMapping("/register")
    public String registerConfirm(@Valid @ModelAttribute("userAddBindingModel")UserAddBindingModel userAddBindingModel,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors() || !userAddBindingModel.getConfirmPassword().equals(userAddBindingModel.getPassword())){
            redirectAttributes.addFlashAttribute("userAddBindingModel",userAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userAddBindingModel",bindingResult);
            return "redirect:/users/register";
        }
       this.userService.register(this.modelMapper.map(userAddBindingModel, UserServiceModel.class));
        return "redirect:/home";

    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView showUserDetails(Principal principal,ModelAndView modelAndView){
        User entity = this.userService.findUserByUsername(principal.getName());
        UsersAllViewModel user = this.modelMapper.map(entity,
                UsersAllViewModel.class);
        user.setRoles(entity.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet()));
        modelAndView.addObject("user",user);
        modelAndView.setViewName("users/profile");
        return modelAndView;
    }

    @GetMapping("/roster")
    @PreAuthorize("isAuthenticated()")
    public String showRoster(Model model){
        model.addAttribute("rosterData",this.rosterService.getLastRoster());
        return "users/roster";
    }
}
