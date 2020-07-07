package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.ProductRepository;
import com.chakarova.demo.model.binding.ProductAddBindingModel;
import com.chakarova.demo.model.entity.Product;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.model.service.ProductServiceModel;
import com.chakarova.demo.model.view.ProductAllViewModel;
import com.chakarova.demo.service.CategoryService;
import com.chakarova.demo.service.CloudinaryService;
import com.chakarova.demo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ProductServiceImpl(ModelMapper modelMapper, ProductRepository productRepository, CategoryService categoryService, CloudinaryService cloudinaryService) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public ProductServiceModel findProductByName(String name) {
        return this.modelMapper.map(this.productRepository.findByName(name),ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> findProductsByCategory(CategoryNames category) {
        return this.productRepository.findByCategory(this.categoryService.findCategoryByName(category))
                .stream().map(x->this.modelMapper.map(x,ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProductServiceModel addProduct(ProductAddBindingModel productAddBindingModel) throws IOException {
        ProductServiceModel productServiceModel = this.modelMapper.map(productAddBindingModel,ProductServiceModel.class);
        productServiceModel.setCategory(this.categoryService.findCategoryByName(CategoryNames.valueOf(productAddBindingModel.getCategory())));
        productServiceModel.setPictureUrl(this.cloudinaryService.uploadImage(productAddBindingModel.getPictureUrl()));

       Product product = this.productRepository.save(this.modelMapper.map(productServiceModel, Product.class));
       return this.modelMapper.map(product,ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> findAllProducts() {
        return this.productRepository.findAll()
                .stream().map(x->this.modelMapper.map(x,ProductServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductAllViewModel> findAllHotDrinks() {
        return this.extractProductsByCategory(CategoryNames.HOT_DRINK.name());
    }

    @Override
    public ProductServiceModel findProductById(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(()->new IllegalArgumentException("No such product"));

        return this.modelMapper.map(product,ProductServiceModel.class);
    }

    @Override
    public void updateProduct(ProductAddBindingModel productAddBindingModel) {
        ProductServiceModel productServiceModel = this.modelMapper.map(productAddBindingModel,ProductServiceModel.class);
        productServiceModel.setCategory(this.categoryService.findCategoryByName(CategoryNames.valueOf(productAddBindingModel.getCategory())));
        productServiceModel.setPictureUrl(this.productRepository.findByName(productAddBindingModel.getName()).getPictureUrl());
        this.productRepository.save(this.modelMapper.map(productServiceModel,Product.class));

    }

    @Override
    public void deleteProduct(Long id) {
        this.productRepository.deleteById(id);
    }

    @Override
    public List<ProductAllViewModel> findAllColdDrinks() {
        return this.extractProductsByCategory(CategoryNames.COLD_DRINK.name());
    }

    @Override
    public  List<ProductAllViewModel>  findAllSnacks() {
        return this.extractProductsByCategory(CategoryNames.SNACK.name());

    }

    @Override
    public List<ProductAllViewModel> findAllCakes() {
        return this.extractProductsByCategory(CategoryNames.CAKE.name());
    }

    private List<ProductAllViewModel> extractProductsByCategory(String category){
        return this.productRepository.findByCategory(this.categoryService.findCategoryByName(CategoryNames.valueOf(category)))
                .stream()
                .map(p->{
                    ProductAllViewModel productViewModel = this.modelMapper.map(p,ProductAllViewModel.class);
                    productViewModel.setCategory(p.getCategory().getCategory().name());
                    return productViewModel;
                })
                .collect(Collectors.toList());
    }
}
