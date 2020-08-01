package com.chakarova.demo.service;

import com.chakarova.demo.dao.RoleRepository;
import com.chakarova.demo.model.entity.Role;
import com.chakarova.demo.service.impl.RoleServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleServiceTests {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void roleService_seedRolesInDb_returnsCorrectCount(){
        RoleService roleServiceToTest = new RoleServiceImpl(roleRepository);
        roleServiceToTest.seedRolesInDb();
        Assert.assertEquals(3,roleRepository.count());
    }

    @Test
    public void roleService_findRoleByName_returnsCorrectRole(){
        RoleService roleServiceToTest = new RoleServiceImpl(roleRepository);
        roleServiceToTest.seedRolesInDb();
        Role expected = roleServiceToTest.findRoleByName("ROLE_USER");
        Role actual = this.roleRepository.findAll().get(0);
        Assert.assertEquals(expected.getAuthority(),actual.getAuthority());

    }
    @Test(expected = Exception.class)
    public void roleService_addRole_ShouldThrowExceptionWhenEmptyDB(){
        RoleService roleServiceToTest = new RoleServiceImpl(this.roleRepository);
        roleRepository.findAll().get(0);

    }

}
