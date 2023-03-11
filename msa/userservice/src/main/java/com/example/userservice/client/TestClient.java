package com.example.userservice.client;

import com.example.userservice.domain.vo.TestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "test", url = "https://jsonplaceholder.typicode.com")
public interface TestClient {

    @GetMapping("/todos/1")
    TestResponse getResponse();
}
