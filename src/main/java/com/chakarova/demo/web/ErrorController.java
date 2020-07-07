package com.chakarova.demo.web;

import org.springframework.web.bind.annotation.GetMapping;

public class ErrorController {
    @GetMapping("/error")
    public String showErrorPage(){
        return "error";
    }
}
