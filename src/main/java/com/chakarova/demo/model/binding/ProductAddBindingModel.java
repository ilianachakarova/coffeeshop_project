package com.chakarova.demo.model.binding;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class ProductAddBindingModel {
    private String name;
    private String description;
    private String category;
    private BigDecimal deliveryPrice;
    private BigDecimal sellPrice;
    private MultipartFile pictureUrl;
    private Integer quantity;

    public ProductAddBindingModel() {
    }
    @NotNull(message = "This field is required")
    @Length(min = 3, message = "Product name should be at least 3 symbols")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min =10, message = "Product description should be at least 10 symbols")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @NotNull(message = "This field is required")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    @NotNull(message = "This field is required")
    @DecimalMin(value = "0.01",message = "Price should be a positive number")
    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
    @NotNull(message = "This field is required")
    @DecimalMin(value = "0.01",message = "Price should be a positive number")
    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public MultipartFile getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(MultipartFile pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    @NotNull(message = "This field is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
