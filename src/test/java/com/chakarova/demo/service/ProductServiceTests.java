package com.chakarova.demo.service;

import com.chakarova.demo.dao.CategoryRepository;
import com.chakarova.demo.dao.ProductRepository;
import com.chakarova.demo.model.binding.ProductAddBindingModel;
import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.model.service.ProductServiceModel;
import com.chakarova.demo.model.view.ProductAllViewModel;
import com.chakarova.demo.service.impl.ProductServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductServiceTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private CloudinaryService cloudinaryService;
    private ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
    }
    private ProductAddBindingModel setUpProduct(){
        ProductAddBindingModel model = new ProductAddBindingModel();
        Category category = new Category();
        category.setCategory(CategoryNames.COLD_DRINK);
        this.categoryRepository.saveAndFlush(category);
        when(categoryService.findCategoryByName(CategoryNames.COLD_DRINK)).thenReturn(category);

        model.setId(1L);
        model.setName("sample");
        model.setDescription("asd asd asd asd asd asd");
        model.setCategory(CategoryNames.COLD_DRINK.name());
        model.setQuantity(100);
        model.setDeliveryPrice(BigDecimal.valueOf(1.0));
        model.setSellPrice(BigDecimal.valueOf(2.0));
        MockMultipartFile imageFile = new MockMultipartFile("data", "filename.txt", "img", "some xml".getBytes());
        model.setPictureUrl(imageFile);

        return model;

    }

    private ProductAddBindingModel setUpInvalidProduct() {
        ProductAddBindingModel model = new ProductAddBindingModel();
        Category category = new Category();
        category.setCategory(CategoryNames.COLD_DRINK);
        this.categoryRepository.saveAndFlush(category);
        when(categoryService.findCategoryByName(CategoryNames.COLD_DRINK)).thenReturn(category);

        model.setId(1L);
        model.setName(null);
        model.setDescription(null);
        model.setCategory("hfudi");
        model.setQuantity(100);
        model.setDeliveryPrice(BigDecimal.valueOf(1.0));
        model.setSellPrice(BigDecimal.valueOf(2.0));
        MockMultipartFile imageFile = new MockMultipartFile("data", "filename.txt", "img", "some xml".getBytes());
        model.setPictureUrl(imageFile);

        return model;
    }

    @Test
    public void productService_addProduct_shouldWorkCorrectlyWithValidInput() throws IOException {
       ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
       ProductAddBindingModel product =  setUpProduct();

       productServiceToTest.addProduct(product);

        Assert.assertEquals(productRepository.count(),1);
        Assert.assertEquals(product.getName(),productRepository.findAll().get(0).getName());
        Assert.assertEquals(product.getDescription(),productRepository.findAll().get(0).getDescription());
        Assert.assertEquals(product.getCategory().toString(),productRepository.findAll().get(0).getCategory().getCategory().toString());
    }

    @Test(expected = Exception.class)
    public void productService_addProduct_shouldThrowWithInvalidInput() throws IOException {
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpInvalidProduct();

        productServiceToTest.addProduct(product);

    }

    @Test
    public void productService_findAllProducts_shouldWorkCorrectlyWhenAtLeastOneProductInDb() throws IOException {
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProduct();

        productServiceToTest.addProduct(product);

        Assert.assertEquals(productRepository.count(),1);
    }

    @Test
    public void productService_findAllProducts_shouldReturnEmptyArrayListWhenEmpty(){
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProduct();

        Assert.assertEquals(productRepository.findAll(),new ArrayList<>());
    }

    @Test
    public void productService_findAllHotDrinks_ShouldWorkCorrectly() throws IOException {
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProductHotDrink();
        productServiceToTest.addProduct(product);

        List<ProductAllViewModel>products = productServiceToTest.findAllHotDrinks();

        Assert.assertEquals(products.size(),1);
    }

    @Test
    public void productService_findAllHotDrinkProducts_shouldReturnEmptyArrayListWhenEmpty(){
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProductHotDrink();

        Assert.assertEquals(productRepository.findAll(),new ArrayList<>());
    }

    @Test
    public void productService_findAllColdDrinks_ShouldWorkCorrectly() throws IOException {
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProduct();
        productServiceToTest.addProduct(product);

        List<ProductAllViewModel>products = productServiceToTest.findAllColdDrinks();

        Assert.assertEquals(products.size(),1);
    }

    //todo: findAllCakes, findAllSnacks

    @Test
    public void productService_findProductById_shouldWorkCorrectlyWithValidId() throws IOException {
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProduct();
        productServiceToTest.addProduct(product);

        ProductServiceModel expected = productServiceToTest.findProductById(1L);
        ProductServiceModel actual = this.modelMapper.map(this.productRepository.findAll().get(0),ProductServiceModel.class);

        Assert.assertEquals(expected.getName(),actual.getName());
        Assert.assertEquals(expected.getCategory(),actual.getCategory());
        Assert.assertEquals(expected.getId(),actual.getId());
        Assert.assertEquals(expected.getDeliveryPrice(),actual.getDeliveryPrice());
        Assert.assertEquals(expected.getSellPrice(),actual.getSellPrice());
        Assert.assertEquals(expected.getSellPrice(),actual.getSellPrice());
        Assert.assertEquals(expected.getDescription(),actual.getDescription());
        Assert.assertEquals(expected.getPictureUrl(),actual.getPictureUrl());
    }

    @Test(expected = Exception.class)
    public void productService_findProductById_shouldThrowWithInvalidId() throws IOException {
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProduct();
        productServiceToTest.addProduct(product);

        ProductServiceModel expected = productServiceToTest.findProductById(10L);

    }

    @Test
    public void productService_deleteProduct_shouldWorkCorrectlyWithValidInput() throws IOException {
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProduct();
        productServiceToTest.addProduct(product);

        productServiceToTest.deleteProduct(1L);

        Assert.assertEquals(this.productRepository.count(),0);
    }

    @Test(expected = Exception.class)
    public void productService_deleteProduct_shouldThrowWithInvalidInput() throws IOException {
        ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
        ProductAddBindingModel product =  setUpProduct();
        productServiceToTest.addProduct(product);

        productServiceToTest.deleteProduct(10L);

    }
    @Test
    public void productService_updateProduct_shouldWorkCorrectlyWithValidInput() throws IOException {

            ProductService productServiceToTest = new ProductServiceImpl(modelMapper,productRepository,categoryService,cloudinaryService);
            ProductAddBindingModel product =  setUpProduct();
            productServiceToTest.addProduct(product);

            ProductAddBindingModel toUpdate = new ProductAddBindingModel();
            toUpdate.setId(product.getId());
            toUpdate.setName(product.getName());
            toUpdate.setDescription(product.getDescription());
            toUpdate.setQuantity(product.getQuantity());
            toUpdate.setCategory(product.getCategory());
        toUpdate.setPictureUrl(product.getPictureUrl());
            toUpdate.setSellPrice(BigDecimal.valueOf(2.2));
            toUpdate.setDeliveryPrice(BigDecimal.valueOf(3.5));
            productServiceToTest.updateProduct(toUpdate);

            Assert.assertEquals(this.productRepository.count(),1);

    }


    private ProductAddBindingModel setUpProductHotDrink() {
        ProductAddBindingModel model = new ProductAddBindingModel();
        Category category = new Category();
        category.setCategory(CategoryNames.HOT_DRINK);
        this.categoryRepository.saveAndFlush(category);
        when(categoryService.findCategoryByName(CategoryNames.HOT_DRINK)).thenReturn(category);

        model.setId(1L);
        model.setName("sample");
        model.setDescription("asd asd asd asd asd asd");
        model.setCategory(CategoryNames.HOT_DRINK.name());
        model.setQuantity(100);
        model.setDeliveryPrice(BigDecimal.valueOf(1.0));
        model.setSellPrice(BigDecimal.valueOf(2.0));
        MockMultipartFile imageFile = new MockMultipartFile("data", "filename.txt", "img", "some xml".getBytes());
        model.setPictureUrl(imageFile);

        return model;
    }
}
