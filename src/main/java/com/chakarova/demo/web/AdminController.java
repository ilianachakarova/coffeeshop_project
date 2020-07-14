package com.chakarova.demo.web;

import com.chakarova.demo.model.binding.ProductAddBindingModel;
import com.chakarova.demo.model.binding.SalesBindingModel;
import com.chakarova.demo.model.binding.UpdateUserBindingModel;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.service.ProductServiceModel;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.model.view.ProductDetailsViewModel;
import com.chakarova.demo.model.view.UsersAllViewModel;
import com.chakarova.demo.service.OrderService;
import com.chakarova.demo.service.ProductService;
import com.chakarova.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final OrderService orderService;

    @Autowired
    public AdminController(UserService userService, ProductService productService, ModelMapper modelMapper, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.orderService = orderService;
    }

    @GetMapping("/all-users")
    public ModelAndView showAllEmployees(ModelAndView modelAndView, Principal principal, Model model) {
        modelAndView.addObject("users", this.userService.findAllUsers());
        modelAndView.addObject("username", principal.getName());
        modelAndView.setViewName("admin/all-users");
        return modelAndView;
    }


    @GetMapping("/update/user/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView userUpdate(@PathVariable("id") Long id, ModelAndView modelAndView, Model model, Principal principal) {
        UserServiceModel userServiceModel = this.userService.findUserById(id);
        UsersAllViewModel user = this.modelMapper.map(userServiceModel, UsersAllViewModel.class);
        modelAndView.addObject("username", principal.getName());
        user.setRoles(userServiceModel.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet()));
        modelAndView.addObject("user", user);
        if (!model.containsAttribute("updateUserBindingModel")) {
            model.addAttribute("updateUserBindingModel", new UpdateUserBindingModel());
        }
        modelAndView.setViewName("admin/employee-update");

        return modelAndView;
    }

    @PostMapping("/update/user/{id}")
    public String confirmUserUpdate(@PathVariable("id") Long id, Model model,
                                    @ModelAttribute("updateUserBindingModel") UpdateUserBindingModel updateUserBindingModel,
                                    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("updateUserBindingModel", updateUserBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateUserBindingModel", bindingResult);
            return "redirect:/admin/update/user/{id}";
        }

        this.userService.updateUser(updateUserBindingModel, id);
        return "redirect:/admin/all-users";
    }

    @GetMapping("/user/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String deleteEmployee(@PathVariable("id") Long id) {
        this.userService.deleteUserById(id);
        return "redirect:/admin/all-users";
    }

    @GetMapping("product/details/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ModelAndView showDetails(@PathVariable("id") Long id, ModelAndView modelAndView) {

        ProductServiceModel productServiceModel = this.productService.findProductById(id);
        ProductDetailsViewModel product = this.modelMapper.map(productServiceModel, ProductDetailsViewModel.class);
        product.setCategory(productServiceModel.getCategory().getCategory().name());

        modelAndView.addObject("product", product);
        modelAndView.setViewName("admin/product-details");
        return modelAndView;
    }

    @GetMapping("product/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public ModelAndView updateProduct(@PathVariable("id") Long id, ModelAndView modelAndView, Principal principal) {
        modelAndView.addObject("username", principal.getName());
        ProductServiceModel productServiceModel = this.productService.findProductById(id);
        ProductDetailsViewModel product = this.modelMapper.map(productServiceModel, ProductDetailsViewModel.class);
        product.setCategory(productServiceModel.getCategory().getCategory().name());
        modelAndView.addObject("productAddBindingModel", product);
        modelAndView.setViewName("admin/product-update");
        return modelAndView;
    }

    @PostMapping("product/details/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("productAddBindingModel") ProductAddBindingModel productAddBindingModel, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productAddBindingModel", productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel", bindingResult);
            return "redirect:/product/details/{id}";
        }
        this.productService.updateProduct(productAddBindingModel);
        return "redirect:/home";
    }

    @GetMapping("product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        this.productService.deleteProduct(id);
        return "redirect:/products/all";
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public String showSales(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        if (!model.containsAttribute("salesBindingModel")) {
            model.addAttribute("salesBindingModel", new SalesBindingModel());
        }
        return "admin/sales-form";
    }

    @PostMapping("/revenue")
    public String confirmShowSales(Model model, @ModelAttribute("salesBindingModel") SalesBindingModel
            salesBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("salesBindingModel", salesBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.salesBindingModel", bindingResult);
            return "redirect:/admin/revenue";
        }
        BigDecimal revenue =
                this.orderService.findTotalRevenueForPeriod(salesBindingModel.getStartDate(),
                        salesBindingModel.getEndDate());
        model.addAttribute("revenue",revenue);
        model.addAttribute("ordersCountPerEmployee",this.orderService.
                findRevenueByEmployee(salesBindingModel.getStartDate(),salesBindingModel.getEndDate()));

        return "admin/sales";
    }


}
