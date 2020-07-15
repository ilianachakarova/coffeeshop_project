package com.chakarova.demo.model.binding;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FindOrderBindingModel {
    private Long orderId;
    private String userPassword;

    public FindOrderBindingModel() {
    }
    @NotNull(message = "This field is required")
    @Min(value = 1,message = "Id should be positive")
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    @NotNull(message = "This field is required")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
