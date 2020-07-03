package com.chakarova.demo.service;

import com.chakarova.demo.model.binding.ProductAddBindingModel;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.model.service.ProductServiceModel;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductServiceModel findProductByName(String name);
    List<ProductServiceModel>findProductsByCategory(CategoryNames category);

    ProductServiceModel addProduct(ProductAddBindingModel productAddBindingModel) throws IOException;
}
