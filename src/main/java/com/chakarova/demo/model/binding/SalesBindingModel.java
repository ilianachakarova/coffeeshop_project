package com.chakarova.demo.model.binding;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

public class SalesBindingModel {
    @NotNull(message = "This field is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent(message = "Birth date should be in the past")
    private LocalDateTime startDate;
    @NotNull(message = "This field is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @PastOrPresent(message = "Birth date should be in the past")
    private LocalDateTime endDate;

    public SalesBindingModel() {
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
