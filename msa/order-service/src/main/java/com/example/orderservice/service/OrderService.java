package com.example.orderservice.service;

import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.entity.Order;

import java.util.List;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto getOrderByOrderId(String orderId);

    List<Order> getOrdersByUsername(String username);
}
