package com.example.userservice.domain.vo;

import lombok.Data;

@Data
public class TestResponse {

    private Long userId;

    private Long id;

    private String title;

    private boolean completed;
}
