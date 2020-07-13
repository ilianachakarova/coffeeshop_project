package com.chakarova.demo.web;

import com.chakarova.demo.model.view.ProductAllViewModel;
import com.chakarova.demo.service.OrderService;
import com.chakarova.demo.service.ProductService;
import com.chakarova.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class RestController {
    //cold-drinks
    //cakes
    //snacks

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;


    @Autowired
    public RestController(ProductService productService, OrderService orderService, UserService userService) {
        this.productService = productService;
        this.orderService = orderService;

        this.userService = userService;
    }

    @GetMapping(value = "/fetch/hot-drinks",produces = "application/json")
    @ResponseBody
    public List<ProductAllViewModel> fetchDataHotDrinks(){
        return this.productService.findAllHotDrinks();
    }

    @GetMapping(value = "/fetch/cold-drinks",produces = "application/json")
    @ResponseBody
    public List<ProductAllViewModel> fetchDataColdDrinks(){
        return this.productService.findAllColdDrinks();
    }

    @GetMapping(value = "/fetch/snacks",produces = "application/json")
    @ResponseBody
    public List<ProductAllViewModel> fetchDataSnacks(){
        return this.productService.findAllSnacks();
    }

    @GetMapping(value = "/fetch/cakes",produces = "application/json")
    @ResponseBody
    public List<ProductAllViewModel> fetchDataCakes(){
        return this.productService.findAllCakes();
    }

    @PostMapping(value = "/array", produces = "application/json")
    public String controllerMethod(@RequestParam(value="myArray[]") String[] myArray, Principal principal){
        List<Long> productIds = Stream.of(myArray).map(Long::valueOf).collect(Collectors.toList());
     this.orderService.createOrder(productIds,this.userService.findUserByUsername(principal.getName()) );
     return "redirect:/orders/receipt";
    }
}
