package com.chakarova.demo.config;

import com.chakarova.demo.model.service.ProductServiceModel;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;


@Configuration
@Aspect
public class AspectApplicationConfig {


    Path path = Paths.get("src/main/resources/files/new-products.txt");
    OutputStream out;

    {
        try {

            out = Files.newOutputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    PrintWriter printWriter = new PrintWriter(out,true);



    @AfterReturning(pointcut = "execution(* com.chakarova.demo.service.impl.ProductServiceImpl.addProduct(..))",returning = "returnValue")
    public void newProductRecord(@NotNull ProductServiceModel returnValue){

        printWriter.println(Instant.now() + " New item added: "+ returnValue.getName());


    }
}
