package com.chakarova.demo.web;

import com.chakarova.demo.model.service.OrderServiceModel;
import com.chakarova.demo.model.view.OrderViewModel;
import com.chakarova.demo.model.view.ProductDetailsViewModel;
import com.chakarova.demo.service.OrderService;
import com.chakarova.demo.service.ProductService;
import com.chakarova.demo.service.impl.OrderServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    @Autowired
    public OrderController(ProductService productService, ModelMapper modelMapper, OrderService orderService) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }

    @GetMapping("/add")
    public ModelAndView addOrder(ModelAndView modelAndView, Principal principal){
        modelAndView.addObject("username", principal.getName());

        modelAndView.addObject("products",this.productService.findAllProducts()
        .stream().map(p->this.modelMapper.map(p, ProductDetailsViewModel.class)).collect(Collectors.toList()));
        modelAndView.setViewName("orders/order-add");
        return modelAndView;
    }

    @GetMapping("/receipt")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView  showCurrentReceipt(ModelAndView modelAndView, Principal principal) throws InterruptedException {
        modelAndView.addObject("username", principal.getName());
        OrderViewModel orderViewModel = this.mapToOrderViewModel(this.orderService.findLastSavedOrder());
        modelAndView.addObject("order",orderViewModel);
        modelAndView.setViewName("orders/order-receipt");
        return modelAndView;
    }

    private OrderViewModel mapToOrderViewModel(OrderServiceModel lastSavedOrder) {
        BigDecimal total = OrderServiceImpl.findTotalPrice(lastSavedOrder.getProducts());
        OrderViewModel orderViewModel = new OrderViewModel();
        orderViewModel.setId(lastSavedOrder.getId());
        orderViewModel.setEmployee(lastSavedOrder.getEmployee().getUsername());
        orderViewModel.setTotal(total);
        orderViewModel.setTimeClosed(lastSavedOrder.getTimeClosed().toString());
        orderViewModel.setProducts(lastSavedOrder.getProducts()
                .stream().map(p->this.modelMapper.map(p, ProductDetailsViewModel.class)).collect(Collectors.toList()));
        return orderViewModel;
    }
}
