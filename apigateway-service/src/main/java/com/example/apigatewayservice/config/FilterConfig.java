package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {

//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r ->
                        r.path("/first-service/**") // 해당 url 로 요청이 들어왔을 때 적용함
                        .filters(f -> // 필터를 적용할 때 사용
                                f.addRequestHeader("first-request", "first-request-header")
                                .addResponseHeader("first-response", "first-response-header"))
                        .uri("http://localhost:8081")) // 해당하는 uri 로 라우팅
                .route(r ->
                        r.path("/second-service/**") // 해당 url 로 요청이 들어왔을 때 적용함
                                .filters(f -> // 필터를 적용할 때 사용
                                        f.addRequestHeader("second-request", "second-request-header")
                                                .addResponseHeader("second-response", "second-response-header"))
                                .uri("http://localhost:8082")) // 해당하는 uri 로 라우팅
                .build(); // 마지막에 꼭 붙여줘야함
    }
}
