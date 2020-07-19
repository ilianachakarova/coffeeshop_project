package com.chakarova.demo.dao;

import com.chakarova.demo.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
//   @Query("select o.products.size from Order as " +
//           "o where o.timeClosed >=:t1 and o.timeClosed<=:t2")
    List<Order>findAllByTimeClosedBetween(LocalDateTime t1,LocalDateTime t2);
    List<Order>findAllByEmployee_Username(String username);
}
