package com.example.userservice.domain.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseOrder {

    private String productId;

    private Integer qty;

    private Integer unitPrice;

    private Integer totalPrice;

    private LocalDate createdDate;

    private String orderId;
}
