package com.chakarova.demo.web;

import com.chakarova.demo.model.binding.FindOrderBindingModel;
import com.chakarova.demo.model.service.OrderServiceModel;
import com.chakarova.demo.model.view.OrderViewModel;
import com.chakarova.demo.model.view.ProductDetailsViewModel;
import com.chakarova.demo.service.OrderService;
import com.chakarova.demo.service.ProductService;
import com.chakarova.demo.service.UserService;
import com.chakarova.demo.service.impl.OrderServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    @Autowired
    public OrderController(ProductService productService, ModelMapper modelMapper, OrderService orderService, BCryptPasswordEncoder encoder, UserService userService) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
        this.encoder = encoder;
        this.userService = userService;
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

    @GetMapping("/find")
    @PreAuthorize("isAuthenticated()")
    public String findOrder(Model model){
        if(!model.containsAttribute("findOrderBindingModel")){
            model.addAttribute("findOrderBindingModel",new FindOrderBindingModel());
        }
       return "orders/order-find";
    }

    @PostMapping("/find")
    public String confirmFindOrder( @ModelAttribute("findOrderBindingModel") FindOrderBindingModel findOrderBindingModel,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                    Principal principal, Model model){
        if(bindingResult.hasErrors()){
           redirectAttributes.addFlashAttribute("findOrderBindingModel",findOrderBindingModel);
           redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.findOrderBindingModel",bindingResult);

           return "redirect:/orders/find";
        }
        if(!encoder.matches(findOrderBindingModel.getUserPassword(),this.userService.findUserByUsername(principal.getName()).getPassword())){
            model.addAttribute("incorrectPd",true);
            return "redirect:/orders/find";
        }
        OrderServiceModel order = this.orderService.findOrderById(findOrderBindingModel.getOrderId());
        if(!order.getEmployee().getUsername().equals(principal.getName())){
            model.addAttribute("incorrectPd",true);
            return "redirect:/orders/find";
        }else {
            model.addAttribute("order",order);
            model.addAttribute("total", OrderServiceImpl.findTotalPrice(order.getProducts()));
            return "orders/order-details";
        }
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
