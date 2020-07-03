package com.chakarova.demo.web;

import com.chakarova.demo.model.binding.ProductAddBindingModel;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

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

}
