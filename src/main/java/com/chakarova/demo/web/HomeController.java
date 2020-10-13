package com.chakarova.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/home")
    public ModelAndView home(Principal principal,ModelAndView modelAndView){
        modelAndView.addObject("username",principal.getName());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @GetMapping("/contacts")
    public ModelAndView contacts(ModelAndView modelAndView){
        modelAndView.setViewName("contacts");
        return modelAndView;
    }
}
