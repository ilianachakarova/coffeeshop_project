package com.chakarova.demo.web;

import com.chakarova.demo.model.binding.ProductAddBindingModel;
import com.chakarova.demo.model.view.ProductAllViewModel;
import com.chakarova.demo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final ModelMapper modelMapper;
    private final ProductService productService;


    @Autowired
    public ProductController(ModelMapper modelMapper, ProductService productService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_ADMIN')")
    public String addProduct(Model model){
        if(!model.containsAttribute("productAddBindingModel")){
            model.addAttribute("productAddBindingModel",new ProductAddBindingModel());
        }
        return "products/product-add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_ADMIN')")
    public String confirmAddProduct(@Valid @ModelAttribute("productAddBindingModel")ProductAddBindingModel
                                                productAddBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws IOException {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("productAddBindingModel",productAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productAddBindingModel",bindingResult);
            return "redirect:/product/add";
        }

        this.productService.addProduct(productAddBindingModel);
        return "redirect:/home";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ROOT','ROLE_ADMIN')")
    public ModelAndView showAllProducts(ModelAndView modelAndView, Principal principal){
        List<ProductAllViewModel>products = this.productService.findAllProducts().stream()
                .map(x->{
                    ProductAllViewModel model = this.modelMapper.map(x,ProductAllViewModel.class);
                    model.setCategory(x.getCategory().getCategory().name());
                    return model;
                }).collect(Collectors.toList());

        modelAndView.addObject("products",products);
        modelAndView.addObject("username",principal.getName());
        modelAndView.setViewName("products/all-products-admin");
        return modelAndView;
    }

}
