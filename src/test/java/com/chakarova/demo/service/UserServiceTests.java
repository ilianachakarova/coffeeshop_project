package com.chakarova.demo.service;

import com.chakarova.demo.dao.RoleRepository;
import com.chakarova.demo.dao.UserRepository;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.entity.enums.RoleNames;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.model.view.UsersAllViewModel;
import com.chakarova.demo.service.impl.UserServiceImpl;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Mock
    private RoleService roleService;
    private ModelMapper modelMapper;

    private BCryptPasswordEncoder encoder;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        encoder = new BCryptPasswordEncoder();
    }

    private UserServiceModel registerUser() {
        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);
        UserServiceModel user = new UserServiceModel();

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

        return userServiceToTest.register(user);
    }

    @Test
    public void userService_registerUser_returnsCorrectValueWithValidInput() {
        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);
        UserServiceModel user = new UserServiceModel();

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

        UserServiceModel expected = userServiceToTest.register(user);
        //UserServiceModel actual = this.modelMapper.map(this.userRepository.findAll().get(0),UserServiceModel.class);
        Assert.assertEquals(userRepository.count(), 1);
        Assert.assertEquals(expected.getEmail(), user.getEmail());
        Assert.assertEquals(expected.getUsername(), user.getUsername());
        Assert.assertEquals(expected.getBirthDate(), user.getBirthDate());
    }

    @Test
    public void userService_findAllUsers_shouldReturnCorrectValue() {
        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);
        registerUser();

        List<UsersAllViewModel> users = userServiceToTest.findAllUsers();

        Assert.assertEquals(users.size(), 1);

    }

    @Test
    public void userService_findUserById_ShouldWorkCorrectlyWithValidId() {
        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);
        registerUser();
        UserServiceModel expected = userServiceToTest.findUserById(1L);
        UserServiceModel actual = this.modelMapper.map(this.userRepository.findAll().get(0), UserServiceModel.class);

        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test(expected = Exception.class)
    public void userService_findUserById_ShouldThrowExceptionWithValidId() {
        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);
        registerUser();

        UserServiceModel expected = userServiceToTest.findUserById(15L);

    }

    @Test
    public void userService_findUserByUserName_shouldWorkCorrectlyWithValidInput() {
        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);
        registerUser();
        User expected = userServiceToTest.findUserByUsername("test");
        User actual = this.userRepository.findAll().get(0);

        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test(expected = Exception.class)
    public void userService_findUserByUsername_shouldThrowWithInvalidInput() {
        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);
        registerUser();
        User expected = userServiceToTest.findUserByUsername("blabla");
    }

//    @Test
//    public void userService_updateUser_ShouldWorkCorrectlyWithValidData() {
//        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);
//        UserServiceModel user = new UserServiceModel();
//
//        user.setUsername("test");
//        user.setPassword("test");
//        user.setFirstName("test");
//        user.setLastName("test");
//        user.setEmail("test@abv.bg");
//        user.setAddress("test");
//
//        Role role = new Role("ROLE_ROOT");
//        roleRepository.saveAndFlush(role);
//        when(roleService.findRoleByName("ROLE_ROOT")).thenReturn(role);
//        HashSet<Role> roles = new HashSet<>();
//        roles.add(role);
//        user.setAuthorities(new HashSet<>(roles));
//
//        user.setBirthDate(LocalDate.of(1994, 2, 2));
//        user.setId(1L);
//
//        UserServiceModel expected = userServiceToTest.register(user);
//
//        UpdateUserBindingModel updateUser = new UpdateUserBindingModel();
//        updateUser.setBonus(BigDecimal.valueOf(200));
//        updateUser.setSalary(BigDecimal.valueOf(1000));
//        Role adminRole = new Role("ROLE_ADMIN");
//        roleRepository.saveAndFlush(adminRole);
//        when(roleService.findRoleByName("ROLE_ADMIN")).thenReturn(role);
//        updateUser.setRole("ROLE_ADMIN");
//
//
//        userServiceToTest.updateUser(updateUser, 1L);
//
//        Assert.assertEquals(userRepository.findAll().get(0).getBonus(), updateUser.getBonus());
//    }

    @Test
    public void userService_deleteUserById_shouldWorkCorrectlyWithValidInput() {
        UserService userServiceToTest = new UserServiceImpl(this.userRepository, this.roleService, this.modelMapper, encoder);

        this.registerUser();
        userServiceToTest.deleteUserById(1L);

        Assert.assertEquals(this.userRepository.findAll().size(), 0);
    }
}
