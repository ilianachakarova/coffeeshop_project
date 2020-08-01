package com.chakarova.demo.service;

import com.chakarova.demo.dao.*;
import com.chakarova.demo.model.entity.Category;
import com.chakarova.demo.model.entity.Product;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.model.entity.enums.RoleNames;
import com.chakarova.demo.model.service.OrderServiceModel;
import com.chakarova.demo.model.service.ProductServiceModel;
import com.chakarova.demo.model.view.OrderViewModel;
import com.chakarova.demo.model.view.UsersAllViewModel;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
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
        Role role = new Role("ROLE_USER");
        roleRepository.saveAndFlush(role);
        when(roleService.findRoleByName("ROLE_USER")).thenReturn(role);

        user.setAuthorities(Set.of(roleService.findRoleByName(RoleNames.ROLE_USER.name())));
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
        Assert.assertNotEquals((orderRepository.findAll().get(0).getProducts().get(0).getQuantity()),setupProduct().getQuantity());
    }

    @Test
    public void orderService_findLastOrder_shouldReturnCorrectOrder() throws InterruptedException {
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());

       OrderServiceModel model = orderServiceToTest.findLastSavedOrder();
       Assert.assertEquals(orderRepository.findAll().get(0).getProducts().get(0).getName(),model.getProducts().get(0).getName());
       Assert.assertEquals(orderRepository.findAll().get(0).getEmployee(),model.getEmployee());
       Assert.assertEquals(orderRepository.findAll().get(0).getTimeClosed(),model.getTimeClosed());
    }

    @Test
    public void productService_findOrdersInTimeRange_shouldWorkCorrectlyWithValidInput() throws InterruptedException {
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = LocalDateTime.now().minusMinutes(2L);
        LocalDateTime t2 = LocalDateTime.now();

        LocalDateTime actual = orderRepository.findAll().get(0).getTimeClosed();

        List<OrderServiceModel>orders =  orderServiceToTest.findOrdersInTimeRange(t1,t2);

        Assert.assertEquals(orders.size(),1);
    }

    @Test(expected = Exception.class)
    public void orderService_findOrdersInTimeRange_shouldThrowWithInvalidInput(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = null;
        LocalDateTime t2 = null;

        orderServiceToTest.findOrdersInTimeRange(t1,t2);

    }

    @Test
    public void orderService_findTotalRevenueForPeriod_shouldWorkCorrectlyWithValidInput(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = LocalDateTime.now().minusMinutes(2L);
        LocalDateTime t2 = LocalDateTime.now();

        BigDecimal total = orderServiceToTest.findTotalRevenueForPeriod(t1,t2);

        Assert.assertEquals(total,orderRepository.findAll().get(0).getTotalPrice());
    }
    @Test(expected = Exception.class)
    public void orderService_findRevenueInTimeRange_shouldThrowWithInvalidInput(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = null;
        LocalDateTime t2 = null;

        orderServiceToTest.findTotalRevenueForPeriod(t1,t2);

    }

    @Test
    public void orderService_findTotalRevenueForOneEmployeeForPeriod_shouldWorkCorrectlyWithValidInput(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = LocalDateTime.now().minusMinutes(2L);
        LocalDateTime t2 = LocalDateTime.now();

        orderServiceToTest.createOrder(productsList,setUpUser());
        User user = setUpUser();
        when(userService.findUserByUsername(any())).thenReturn(user);
        BigDecimal total = orderServiceToTest.findTotalRevenueForOneEmployeeForPeriod("test",t1,t2);

        Assert.assertEquals(total,orderRepository.findAll().get(0).getTotalPrice());
    }

    @Test(expected = Exception.class)
    public void orderService_findTotalRevenueForOneEmployeeForPeriod_shouldThrowWithInvalidInput(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = LocalDateTime.now().minusMinutes(2L);
        LocalDateTime t2 = LocalDateTime.now();

        orderServiceToTest.createOrder(productsList,setUpUser());
        User user = setUpUser();
        when(userService.findUserByUsername(any())).thenReturn(user);
        BigDecimal total = orderServiceToTest.findTotalRevenueForOneEmployeeForPeriod(null,null,null);
    }

    @Test
    public void orderService_findOrderById_shouldWorkCorrectlyWithValidInput(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = LocalDateTime.now().minusMinutes(2L);
        LocalDateTime t2 = LocalDateTime.now();

        OrderServiceModel model = orderServiceToTest.findOrderById(1L);

        Assert.assertEquals(model.getProducts().get(0).getName(),
                orderRepository.findAll().get(0).getProducts().get(0).getName());
        Assert.assertEquals(model.getEmployee(),orderRepository.findAll().get(0).getEmployee());
        Assert.assertEquals(model.getTimeClosed(),orderRepository.findAll().get(0).getTimeClosed());
        Assert.assertEquals(model.getId(),orderRepository.findAll().get(0).getId());

    }

    @Test
    public void orderService_findRevenueByEmployee_shouldWorkCorrectly(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = LocalDateTime.now().minusMinutes(2L);
        LocalDateTime t2 = LocalDateTime.now();
        UsersAllViewModel user  = this.modelMapper.map(setUpUser(),UsersAllViewModel.class);
        List<UsersAllViewModel>users = List.of(user);
       when(userService.findAllUsers()).thenReturn(users);

       Map<String,Integer> result = orderServiceToTest.findRevenueByEmployee(t1,t2);

       Assert.assertTrue(result.containsKey(user.getUsername()));

    }

    @Test(expected = Exception.class)
    public void orderService_findRevenueByEmployee_shouldThrowWithInvalidData(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = null;
        LocalDateTime t2 = null;
        UsersAllViewModel user  = this.modelMapper.map(setUpUser(),UsersAllViewModel.class);
        List<UsersAllViewModel>users = List.of(user);
        when(userService.findAllUsers()).thenReturn(users);

        Map<String,Integer> result = orderServiceToTest.findRevenueByEmployee(t1,t2);

        Assert.assertTrue(result.containsKey(user.getUsername()));

    }

    @Test
    public void orderService_findAllOrdersByEmployee_shouldWorkCorrectlyWithValidInput(){
        OrderService orderServiceToTest =
                new OrderServiceImpl(productService,modelMapper,orderRepository,productRepository,userService);
        List<Long>productsList = new ArrayList<>();
        productsList.add(1L);
        ProductServiceModel product = this.modelMapper.map(setupProduct(), ProductServiceModel.class);
        when(productService.findProductById(anyLong())).thenReturn(product);

        orderServiceToTest.createOrder(productsList,setUpUser());
        LocalDateTime t1 = LocalDateTime.now().minusMinutes(2L);
        LocalDateTime t2 = LocalDateTime.now();
        User user  = setUpUser();
       when(userService.findUserByUsername(any())).thenReturn(user);

        List<OrderViewModel> models = orderServiceToTest.findAllOrdersByEmployee("test");

        Assert.assertEquals(models.size(),1);
        Assert.assertEquals(models.get(0).getEmployee(),orderRepository.findAll().get(0).getEmployee().getUsername());
    }

}


