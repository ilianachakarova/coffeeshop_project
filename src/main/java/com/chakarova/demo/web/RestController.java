package com.chakarova.demo.web;

import com.chakarova.demo.model.view.ProductAllViewModel;
import com.chakarova.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @PostMapping(value = "/array",produces = "application/json")
    public String controllerMethod(@RequestParam(value="myArray[]") String[] myArray){
        List<Long> productIds = Stream.of(myArray).map(Long::valueOf).collect(Collectors.toList());
    // this.orderService.createListOfOrderProducts(productIds);
        //todo: map the array to long, these are the ids of the products to be ordered.
        //From these ids, create OrderItem entities as well as an Order Entity
        //Create Order Service and implement method to create order(find all products by id, create order item, decrease quantity of product)
        //return redirect home
       return null;
    }
}
