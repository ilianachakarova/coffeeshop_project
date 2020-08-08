package com.chakarova.demo.web.controller;

import com.chakarova.demo.dao.ProductRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductControllerTests {
    @Autowired
   private MockMvc mvc;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})

    public void add_shouldReturnCorrectView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/product/add"))
                .andExpect(view().name("products/product-add"));
    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})

    public void add_shouldSaveProductInDbCorrectly() throws Exception {
        BufferedImage img;
        img = ImageIO.read(new File("src\\main\\resources\\static\\img\\coffee-shop-details.jpg"));
        WritableRaster raster = img .getRaster();
        DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
        byte[] testImage = data.getData();

        FileInputStream fis = new FileInputStream("src\\main\\resources\\static\\img\\coffee-shop-details.jpg");
        MockMultipartFile image = new MockMultipartFile("image", fis);
        mvc.perform(MockMvcRequestBuilders.multipart("/product/add")
                .file("pictureUrl",testImage)
                .param("name","sample")
                .param("description","jkdsnfkdnidnfieufbiubff")
                .param("deliveryPrice","0.80")
                .param("sellPrice","1.80")
                .param("category","HOT_DRINK")
                .param("quantity","20")

        );
       // mvc.perform(MockMvcRequestBuilders.multipart("/product/add").file(firstFile));

        Assert.assertEquals(1,productRepository.count());

    }

    @Test
    @WithMockUser(value = "spring",roles = {"ROOT"})

    public void all_shouldReturnCorrectView() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/product/all"))
                .andExpect(view().name("products/all-products-admin"));
    }

    @Test

    public void all_shouldReturnCorrectViewWhenUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/product/all"))
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }


}
