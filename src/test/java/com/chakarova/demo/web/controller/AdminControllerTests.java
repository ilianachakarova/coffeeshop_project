package com.chakarova.demo.web.controller;

import com.chakarova.demo.dao.ProductRepository;
import com.chakarova.demo.dao.UserRepository;
import com.chakarova.demo.model.entity.Product;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.entity.enums.CategoryNames;
import com.chakarova.demo.model.entity.enums.RoleNames;
import com.chakarova.demo.service.CategoryService;
import com.chakarova.demo.service.RoleService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdminControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void init(){
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    public void allUsers_shouldReturnCorrectView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/all-users"))
                .andExpect(view().name("admin/all-users"));
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void editUser_WorksCorrectlyWithValidId() throws Exception {
        User testUser = this.setUpUser();
        this.userRepository.saveAndFlush(testUser);
        this.mvc.perform(MockMvcRequestBuilders.post("/admin/update/user/" + testUser.getId())
        .param("salary","2000")
        .param("bonus","300")
                .param("role","ROLE_ADMIN")
        );
        User userActual = this.userRepository.findAll().get(0);
        MathContext m = new MathContext(4);
        Assert.assertEquals(userActual.getBonus().round(m),BigDecimal.valueOf(300.0));
        Assert.assertEquals(userActual.getSalary().round(m),BigDecimal.valueOf(2000));
        Assert.assertEquals(userActual.getAuthorities().size(),1);
    }


    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void editUser_redirectsCorrectly() throws Exception {
        User testUser = this.setUpUser();
        this.userRepository.saveAndFlush(testUser);
        this.mvc.perform(MockMvcRequestBuilders.post("/admin/update/user/" + testUser.getId())
                .param("salary","2000")
                .param("bonus","300")
                .param("role","ROLE_ADMIN")
        ).andExpect(redirectedUrl("/admin/all-users?favicon=%2Fimg%2Ffavicon2-32x32.png"));

    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void delete_shouldWorkCorrectly() throws Exception {
        User testUser = this.setUpUser();
        testUser = this.userRepository.saveAndFlush(testUser);
        this.mvc.perform(MockMvcRequestBuilders.get("/admin/user/delete/" + testUser.getId()));

        Assert.assertEquals(userRepository.count(),0);
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    public void delete_redirectsCorrectly() throws Exception {
        User testUser = this.setUpUser();
        testUser = this.userRepository.saveAndFlush(testUser);
        this.mvc.perform(MockMvcRequestBuilders.get("/admin/user/delete/" + testUser.getId()))
        .andExpect(redirectedUrl("/admin/all-users?favicon=%2Fimg%2Ffavicon2-32x32.png"));
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void productDetails_shouldWorkCorrectlyWithValidInput() throws Exception {
        Product product = setUpProduct();
        product = this.productRepository.saveAndFlush(product);
        mvc.perform(MockMvcRequestBuilders.get("/admin/product/details/"+product.getId()))
                .andExpect(view().name("admin/product-details"));
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void productUpdate_shouldWorkCorrectlyWithValidInput() throws Exception {
        Product product = setUpProduct();
        product = this.productRepository.saveAndFlush(product);
        mvc.perform(MockMvcRequestBuilders.get("/admin/product/update/"+product.getId()))
                .andExpect(view().name("admin/product-update"));
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void productUpdate_postRequestShouldWorkCorrectly() throws Exception {
        Product product = setUpProduct();
        product = this.productRepository.saveAndFlush(product);
        mvc.perform(MockMvcRequestBuilders.post("/admin/product/details/"+product.getId())
        .param("name",product.getName())
                .param("description",product.getDescription())
                .param("category",product.getCategory().getCategory().name())
                .param("deliveryPrice","3.3")
                .param("sellPrice","4.4")
                .param("pictureUrl",product.getPictureUrl())
                .param("quantity","500")
        );
        Assert.assertEquals(1,productRepository.count());
        Assert.assertEquals(productRepository.findAll().get(0).getName(),"sample");
       // Assert.assertEquals(productRepository.findAll().get(0).getQuantity().toString(),"500");
    }
    //todo mock multipart file
//    @Test
//    @WithMockUser(value = "spring",roles = {"ROOT"})
//    public void updateProduct_shouldRedirectCorrectly() throws Exception {
//        Product product = setUpProduct();
//        product = this.productRepository.saveAndFlush(product);
//        mvc.perform(MockMvcRequestBuilders.post("/admin/product/details/"+product.getId())
//                .param("name",product.getName())
//                .param("description",product.getDescription())
//                .param("category",product.getCategory().getCategory().name())
//                .param("deliveryPrice","3.3")
//                .param("sellPrice","4.4")
//                .param("pictureUrl",product.getPictureUrl())
//                .param("quantity","500")
//        ).andExpect(redirectedUrl("/products/all-products-admin?favicon=%2Fimg%2Ffavicon2-32x32.png"));
//    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void deleteProduct_shouldWorkCorrectly() throws Exception {
        Product product = setUpProduct();
        product = this.productRepository.saveAndFlush(product);
        mvc.perform(MockMvcRequestBuilders.get("/admin/product/delete/"+product.getId()));
        Assert.assertEquals(0,productRepository.count());
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void deleteProduct_shouldRedirectCorrectly() throws Exception {
        Product product = setUpProduct();
        product = this.productRepository.saveAndFlush(product);
        mvc.perform(MockMvcRequestBuilders.get("/admin/product/delete/"+product.getId()));
        Assert.assertEquals(0,productRepository.count());
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT","ADMIN"})
    @DirtiesContext
    public void revenue_shouldReturnCorrectView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/revenue"))
                .andExpect(view().name("admin/sales-form"));
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})
    @DirtiesContext
    public void revenue_postRequestShouldReturnCorrectView() throws Exception {
        String str = "2020-08-07 11:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

        mvc.perform(MockMvcRequestBuilders.post("/admin/revenue")
        .param("startDate", String.valueOf(dateTime))
        .param("endDate", String.valueOf(LocalDateTime.now())))
        .andExpect(view().name("redirect:/admin/revenue"));

    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT","ADMIN"})
    @DirtiesContext
    public void roster_shouldReturnCorrectView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/roster"))
                .andExpect(view().name("admin/roster-form"));
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT","ADMIN"})
    @DirtiesContext
    public void roster_postRequestShouldReturnCorrectView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/admin/roster")
        .param("monday","sample")
        .param("tuesday","sample")
        .param("wednesday","sample")
        .param("thursday","sample")
        .param("friday","sample")
                .param("saturday","sample")

        )
                .andExpect(view().name("redirect:/home"));
    }


    private User setUpUser() {
        User user = new User();

        user.setUsername("test");
        user.setPassword("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("test@abv.bg");
        user.setAddress("test");


        user.setAuthorities(Set.of(roleService.findRoleByName(RoleNames.ROLE_USER.name())));
        user.setBirthDate(LocalDate.of(1994, 2, 2));
        user.setId(1L);

        userRepository.saveAndFlush(user);

        return user;
    }

    private Product setUpProduct(){
        Product model = new Product();
        model.setId(1L);
        model.setName("sample");
        model.setDescription("asd asd asd asd asd asd");
        model.setCategory(this.categoryService.findCategoryByName(CategoryNames.HOT_DRINK));
        model.setQuantity(100);
        model.setDeliveryPrice(BigDecimal.valueOf(1.0));
        model.setSellPrice(BigDecimal.valueOf(2.0));
        //MockMultipartFile imageFile = new MockMultipartFile("data", "filename.txt", "img", "some xml".getBytes());
        model.setPictureUrl("https://images.unsplash.com/photo-1594962056279-d800aef783f6?ixlib=rb-1.2.1&auto=format&fit=crop&w=366&q=80");
        return model;
    }


}
