package com.chakarova.demo.web.controller;

import com.chakarova.demo.DemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = DemoApplication.class)
public class HomeControllerTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void index_returnsCorrectValue() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.get("/"))
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser("spring")
    public void home_returnsCorrectValue() throws Exception {
        this.mvc
                .perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(view().name("home"));
    }
}
