package com.chakarova.demo.web.controller;

import com.chakarova.demo.dao.RoleRepository;
import com.chakarova.demo.dao.UserRepository;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.entity.enums.RoleNames;
import com.chakarova.demo.service.RoleService;
import com.chakarova.demo.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserAuthControllerTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;
    private Principal principal;
    private ModelMapper modelMapper;

    @Before
    public void init(){
        modelMapper = new ModelMapper();

    }
    @Test

    public void login_returnsCorrectView() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.get("/users/login"))
                .andExpect(view().name("login"));
    }

    @Test

    public void register_returnsCorrectView() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.get("/users/register"))
                .andExpect(view().name("register"));
    }

    @Test

    public void register_shouldSaveUserInDbCorrectly() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.post("/users/register")
                        .param("username","test")
                        .param("firstName","Test")
                        .param("lastName","Test")
                        .param("password","Test")
                        .param("confirmPassword","Test")
                        .param("email","test@abv.bg")
                        .param("birthDate", String.valueOf(LocalDate.of(1994,8,2)))
                        .param("address","Test str 5")
                );

        Assert.assertEquals(1,this.userRepository.count());
    }

    @Test

    public void register_shouldRedirectCorrectly() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.post("/users/register")
                        .param("username","test")
                        .param("firstName","Test")
                        .param("lastName","Test")
                        .param("password","Test")
                        .param("confirmPassword","Test")
                        .param("email","test@abv.bg")
                        .param("birthDate", String.valueOf(LocalDate.of(1994,8,2)))
                        .param("address","Test str 5")
                ).andExpect(view().name("redirect:/home"));
    }

//    @Test
//    @WithMockUser("spring")
//    public void profile_shouldReturnCorrectView() throws Exception {
//       User model = setUpUser();
//       userRepository.saveAndFlush(model);
//        User userEntity = userService.findUserByUsername("test");
//
//        boolean x = principal.getName().equals("test");
//        UsersAllViewModel user = this.modelMapper.map(userEntity,UsersAllViewModel.class);
//        user.setRoles(Set.of("ROLE_ROOT"));
//        mvc.perform(MockMvcRequestBuilders.get("/users/profile"))
//                .andExpect(view().name("users/profile"));
//    }

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
}
