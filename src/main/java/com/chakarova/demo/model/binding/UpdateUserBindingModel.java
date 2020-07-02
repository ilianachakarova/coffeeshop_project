package com.chakarova.demo.model.binding;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

public class UpdateUserBindingModel {
    private BigDecimal salary;
    private BigDecimal bonus;
    private String role;

    public UpdateUserBindingModel() {
    }
    @DecimalMin(value = "0.01", message = "Salary should be positive")
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    @DecimalMin(value = "0.01", message = "Salary should be positive")
    public BigDecimal getBonus() {
        return bonus;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
