package com.chakarova.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProductNotFoundError extends RuntimeException{
    private int statusCode;

    public ProductNotFoundError(String message) {
        super(message);
        statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
