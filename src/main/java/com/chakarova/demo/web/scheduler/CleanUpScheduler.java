package com.chakarova.demo.web.scheduler;

import com.chakarova.demo.service.OrderService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class CleanUpScheduler {
    private final OrderService orderService;

    public CleanUpScheduler(OrderService orderService) {
        this.orderService = orderService;
    }
    @Scheduled(cron = "0 0 12 1 * ?") // every month on the first
   // @Scheduled(cron = "0 * * ? * *") // TEST every 1 minutes

    public void cleanUpClosedOrders(){

        this.orderService.deleteAllOrders();
    }
}
