package com.example.userservice.controller;

import com.example.userservice.client.TestClient;
import com.example.userservice.domain.vo.TestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TestClient testClient;

    @GetMapping("/test")
    public TestResponse ApiTest() {
        return testClient.getResponse();
    }
}
