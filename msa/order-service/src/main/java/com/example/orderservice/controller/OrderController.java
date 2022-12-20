package com.example.orderservice.controller;

import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.domain.vo.RequestOrder;
import com.example.orderservice.domain.vo.ResponseOrder;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final Environment env;

    @GetMapping("/health_check")
    public String status() {
        return String.format("order-service works on PORT %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{username}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable String username, @RequestBody RequestOrder requestOrder) {
        ModelMapper modelMapper = new ModelMapper();

        OrderDto orderDto = modelMapper.map(requestOrder, OrderDto.class);
        orderDto.setUsername(username);
        ResponseOrder responseOrder = modelMapper.map(orderService.createOrder(orderDto), ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{username}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable String username) {
        ModelMapper modelMapper = new ModelMapper();
        List<Order> orders = orderService.getOrdersByUsername(username);
        List<ResponseOrder> responseOrders = orders.stream().map(order -> modelMapper.map(order, ResponseOrder.class)).collect(Collectors.toList());
        return ResponseEntity.ok(responseOrders);
    }
}
