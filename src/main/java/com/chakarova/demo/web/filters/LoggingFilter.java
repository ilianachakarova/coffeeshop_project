package com.chakarova.demo.web.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

@Component

public class LoggingFilter implements Filter {
    int count = 1;
    Path path = Paths.get(String.format("src/main/resources/files/output%d.txt",count++));
    OutputStream out;

    {
        try {

            out = Files.newOutputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    PrintWriter printWriter = new PrintWriter(out,true);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        printWriter.println(Instant.now().toString() + ": "+ request.getMethod()+ " " + request.getRequestURL().toString());


        filterChain.doFilter(servletRequest,servletResponse);

    }


}
