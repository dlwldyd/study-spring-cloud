package com.example.orderservice.service;

import com.example.orderservice.domain.dto.OrderDto;
import com.example.orderservice.domain.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getUnitPrice() * orderDto.getQty());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Order order = modelMapper.map(orderDto, Order.class);

        orderRepository.save(order);
        return orderDto;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        ModelMapper modelMapper = new ModelMapper();
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new RuntimeException("No such order"));
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUsername(username);
    }
}
