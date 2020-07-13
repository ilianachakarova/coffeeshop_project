package com.chakarova.demo.service;

import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.service.OrderServiceModel;

import java.util.List;

public interface OrderService {


    void createOrder(List<Long> productIds, User employee);
    OrderServiceModel findLastSavedOrder() throws InterruptedException;
}
