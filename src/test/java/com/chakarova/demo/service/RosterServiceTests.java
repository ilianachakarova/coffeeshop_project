package com.chakarova.demo.service;

import com.chakarova.demo.dao.RosterRepository;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.model.entity.Roster;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.service.UserServiceModel;
import com.chakarova.demo.model.view.UsersAllViewModel;
import com.chakarova.demo.service.impl.RosterServiceImpl;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RosterServiceTests {
    @Autowired
    private RosterRepository rosterRepository;

    private ModelMapper modelMapper;

   @Mock
   private UserService userService;
    @Mock
    private RoleService roleService;

    @Before
    public void init(){
        modelMapper = new ModelMapper();
    }

    @Test
    @DirtiesContext
    public void rosterService_addRoster_returnsCorrectValue(){
        RosterService rosterService = new RosterServiceImpl(rosterRepository,modelMapper,userService);

        Roster testRoster = new Roster();
        testRoster.setMonday("test");
        testRoster.setTuesday("test");
        testRoster.setWednesday("test");
        testRoster.setThursday("test");
        testRoster.setFriday("test");
        testRoster.setSaturday("test");

        Roster expected = rosterRepository.save(testRoster);
        Roster actual = rosterRepository.findAll().get(0);

        Assert.assertEquals(rosterRepository.count(),1);

        Assert.assertEquals(expected.getMonday(),actual.getMonday());
        Assert.assertEquals(expected.getTuesday(),actual.getTuesday());
        Assert.assertEquals(expected.getWednesday(),actual.getWednesday());
        Assert.assertEquals(expected.getThursday(),actual.getThursday());
        Assert.assertEquals(expected.getFriday(),actual.getFriday());
        Assert.assertEquals(expected.getSaturday(),actual.getSaturday());
    }


    @Test
    @DirtiesContext
    public void rosterService_getLastRoster_returnsCorrectValue(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("test");
        user.setFirstName("test");
        user.setLastName("test");
        user.setEmail("test@abv.bg");
        user.setAddress("test");
        when(roleService.findRoleByName("ROLE_USER")).thenReturn(new Role("ROLE_USER"));
        user.setAuthorities(Set.of(roleService.findRoleByName("ROLE_USER")));
        user.setBirthDate(LocalDate.of(1994,2,2));
        user.setId(1L);

        when(userService.register(any())).thenReturn(this.modelMapper.map(user,UserServiceModel.class));

        UserServiceModel userServiceModel = userService.register(this.modelMapper.map(user, UserServiceModel.class));
        UsersAllViewModel model = new UsersAllViewModel();
        model.setUsername(userServiceModel.getUsername());
        model.setFirstName(userServiceModel.getFirstName());
        model.setLastName(userServiceModel.getLastName());
        model.setEmail(userServiceModel.getEmail());
        model.setRoles(userServiceModel.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet()));
        model.setBirthDate(userServiceModel.getBirthDate());
        model.setId(userServiceModel.getId().toString());
        List<UsersAllViewModel>all = new ArrayList<>();
        all.add(model);
       when(userService.findAllUsers()).thenReturn(all);

        RosterService rosterService = new RosterServiceImpl(rosterRepository,modelMapper,userService);


        Roster testRoster = new Roster();
        testRoster.setMonday("test");
        testRoster.setTuesday("test");
        testRoster.setWednesday("test");
        testRoster.setThursday("test");
        testRoster.setFriday("test");
        testRoster.setSaturday("test");


        rosterRepository.saveAndFlush(testRoster);
        Map<String, List<String>> expected = rosterService.getLastRoster();
        Roster actual = rosterRepository.findAll().get(0);

        Assert.assertEquals(expected.values().size(),1);
        Assert.assertEquals(expected.containsKey("test"),true);
    }


}
