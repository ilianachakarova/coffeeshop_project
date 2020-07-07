package com.chakarova.demo.web;

import com.chakarova.demo.model.view.ProductDetailsViewModel;
import com.chakarova.demo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    @Autowired
    public OrderController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView addOrder(ModelAndView modelAndView, Principal principal){
        modelAndView.addObject("username", principal.getName());
        modelAndView.addObject("products",this.productService.findAllProducts()
        .stream().map(p->this.modelMapper.map(p, ProductDetailsViewModel.class)).collect(Collectors.toList()));
        modelAndView.setViewName("orders/order-add");
        return modelAndView;
    }
}
