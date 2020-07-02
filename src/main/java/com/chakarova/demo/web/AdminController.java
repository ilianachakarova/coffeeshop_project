package com.chakarova.demo.web;

import com.chakarova.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/all-users")
    public String showAllEmployees(){
        return "all-employees";
    }
}
