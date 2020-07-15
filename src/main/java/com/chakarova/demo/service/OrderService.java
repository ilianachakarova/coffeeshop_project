package com.chakarova.demo.service;

import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.service.OrderServiceModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {


    void createOrder(List<Long> productIds, User employee);
    OrderServiceModel findLastSavedOrder() throws InterruptedException;
    List<OrderServiceModel>findOrdersInTimeRange(LocalDateTime t1,LocalDateTime t2);
    BigDecimal findTotalRevenueForPeriod(LocalDateTime t1,LocalDateTime t2);
    Map<String,Integer> findRevenueByEmployee(LocalDateTime t1, LocalDateTime t2);

    OrderServiceModel findOrderById(Long orderId);

}
