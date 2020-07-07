package com.chakarova.demo.web;

import com.chakarova.demo.model.view.ProductAllViewModel;
import com.chakarova.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RestController {
    //cold-drinks
    //cakes
    //snacks

    public final ProductService productService;

    @Autowired
    public RestController(ProductService productService) {
        this.productService = productService;
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
}
