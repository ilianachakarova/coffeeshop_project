package com.chakarova.demo.model.binding;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class UserAddBindingModel {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
   private LocalDate birthDate;
    private String address;

    public UserAddBindingModel() {
    }
    @NotNull(message = "Please enter a username")
    @Length(min=3,message = "Username must be at least 3 symbols")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @NotNull(message = "Please enter a password")
    @Length(min=3,message = "Password must be at least 3 symbols")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @NotNull(message = "Please enter a confirm password")
    @Length(min=3,message = "Password must be at least 3 symbols")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    @NotNull(message = "Please enter an email")
    @Email(message = "Please enter a valid email address")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @DateTimeFormat(pattern = "yyyy-MM-dd")
   // @PastOrPresent(message = "Birth date should be in the past")
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
