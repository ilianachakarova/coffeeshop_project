package com.chakarova.demo.model.binding;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class FindOrderBindingModel {
    private Long orderId;
    private String userPassword;

    public FindOrderBindingModel() {
    }
    @NotNull(message = "This field is required")
    @Positive(message = "Id should be positive")
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
