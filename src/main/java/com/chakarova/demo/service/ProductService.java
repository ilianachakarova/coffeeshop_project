package com.chakarova.demo.service;

import com.chakarova.demo.model.binding.ProductAddBindingModel;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.model.service.ProductServiceModel;
import com.chakarova.demo.model.view.ProductAllViewModel;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    ProductServiceModel findProductByName(String name);
    List<ProductServiceModel>findProductsByCategory(CategoryNames category);

    ProductServiceModel addProduct(ProductAddBindingModel productAddBindingModel) throws IOException;

    List<ProductServiceModel>findAllProducts();

    List<ProductAllViewModel> findAllHotDrinks();

    ProductServiceModel findProductById(Long id);

    void updateProduct(ProductAddBindingModel productAddBindingModel);

    void deleteProduct(Long id);

    List<ProductAllViewModel> findAllColdDrinks();

    List<ProductAllViewModel> findAllSnacks();

    List<ProductAllViewModel> findAllCakes();


}
