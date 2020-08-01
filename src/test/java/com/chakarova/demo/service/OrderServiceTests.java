package com.chakarova.demo.service;

import com.chakarova.demo.dao.*;
import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.Product;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.model.entity.enums.RoleNames;
import com.chakarova.demo.model.service.ProductServiceModel;
import com.chakarova.demo.service.impl.OrderServiceImpl;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderServiceTests {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private RoleService roleService;

    private ModelMapper modelMapper;

    @Before
    public void init(){
        modelMapper = new ModelMapper();
    }

    private Product setupProduct(){
        Product product = new Product();
        product.setId(1L);
        product.setName("sample");
        product.setDescription("kjfhuif jiudfhfn judbfiui");
        product.setQuantity(400);
        Category category = new Category(CategoryNames.COLD_DRINK);
        categoryRepository.saveAndFlush(category);
        when(categoryService.findCategoryByName(CategoryNames.COLD_DRINK)).thenReturn(category);
        product.setCategory(category);
        product.setDeliveryPrice(BigDecimal.valueOf(2.2));
        product.setSellPrice(BigDecimal.valueOf(3.2));
        product.setPictureUrl("https://image.shutterstock.com/image-photo/successful-football-soccer-basketball-baseball-600w-1034455687.jpg");
        return product;
    }
    private User setUpUser(){
        User user = new User();

        user.setUsername("test");
        user.setPassword("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("test@abv.bg");
        user.setAddress("test");
        Role role = new Role("ROLE_ROOT");
        roleRepository.saveAndFlush(role);
        when(roleService.findRoleByName("ROLE_ROOT")).thenReturn(role);

        user.setAuthorities(Set.of(roleService.findRoleByName(RoleNames.ROLE_ROOT.name())));
        user.setBirthDate(LocalDate.of(1994, 2, 2));
        user.setId(1L);

        userRepository.saveAndFlush(user);

        return(user);
    }

    @Test
    public void orderService_createOrder_worksProperlyWithValidInput(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);

        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());

        Assert.assertEquals(orderRepository.count(),1);
    }

    
}


