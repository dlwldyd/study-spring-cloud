package com.example.orderservice.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {

    private String productId;

    private String qty;

    private Integer unitPrice;

    private Integer totalPrice;

    private LocalDate createdDate;

    private String OrderId;
}
