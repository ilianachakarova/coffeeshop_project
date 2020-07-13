package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.OrderRepository;
import com.chakarova.demo.model.entity.Order;
import com.chakarova.demo.model.entity.Product;
import com.chakarova.demo.model.entity.User;
import com.chakarova.demo.model.service.OrderServiceModel;
import com.chakarova.demo.service.OrderService;
import com.chakarova.demo.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(ProductService productService, ModelMapper modelMapper, OrderRepository orderRepository) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public void createOrder(List<Long> productIds, User employee) {
        //1. extract products
        List<Product>products =
                productIds.stream().map(this.productService::findProductById).
                        map(p->this.modelMapper.map(p,Product.class)).collect(Collectors.toList());

        BigDecimal totalPrice = findTotalPrice(products);

        Order order = new Order(LocalDateTime.now(),products,totalPrice,employee);
        this.orderRepository.saveAndFlush(order);

    }

    @Override
    public OrderServiceModel findLastSavedOrder() {
        Order order = this.orderRepository.findAll().get((int) orderRepository.count()-1);
        return this.modelMapper.map(order,OrderServiceModel.class);
    }

    public static BigDecimal findTotalPrice(List<Product> products) {
        BigDecimal sum = new BigDecimal("0.00");
        for (Product product : products) {
            sum = sum.add(product.getSellPrice());
        }

        return sum;
    }
}
