package com.chakarova.demo.model.entity;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    private String username;
    private String password;
    private String email;
    private LocalDate birthDate;
    private String address;
    private BigDecimal salary;
    private BigDecimal bonus;

    private Set<Role> authorities;

    public User(){

    }

    @Override
    @Column(name = "username", nullable = false,unique = true)
    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id",
                    referencedColumnName = "id"
            )
    )
    public Set<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }
    @Column(name = "salary",nullable = true)
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary){
        this.salary = salary;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }
    @Column(name = "bonus",nullable = true)
    public BigDecimal getBonus() {
        return bonus;
    }

    @Column(name = "address", columnDefinition = "TEXT")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Column(name = "birth_date",nullable = false)
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
