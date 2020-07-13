package com.chakarova.demo.web;

import com.chakarova.demo.model.binding.UserAddBindingModel;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/users")
public class UserAuthController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    @Autowired
    public UserAuthController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
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
}
