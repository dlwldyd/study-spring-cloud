package com.example.apigatewayservice.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {

    public LoggingFilter() {
        super(Config.class);
    }

    // 파라미터로 Config 정보가 들어있는 객체가 들어온다.
    // 필터 작동 순서 : global pre filter -> custom pre filter -> custom post filter -> global post filter
    @Override
    public GatewayFilter apply(Config config) {

        // pre filter
//        return ((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//
//            log.info("Global Filter baseMessage : {}", config.getBaseMessage());
//
//            if (config.isPreLogger()) {
//                log.info("Global Filter start : request id -> {}", request.getId());
//            }
//            // pre filter에 chaining 으로 post filter 가 추가됨
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                if (config.isPreLogger()) {
//                    log.info("Global Filter end : response status code -> {}", response.getStatusCode());
//                }
//            }));
//        });

        GatewayFilter filter = new OrderedGatewayFilter(((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Logging Filter baseMessage : {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Logging Pre Filter : request id -> {}", request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                if (config.isPreLogger()) {
                    log.info("Logging Post Filter : response status code -> {}", response.getStatusCode());
                }
            }));
        }), Ordered.HIGHEST_PRECEDENCE); // 두번째 파라미터(Ordered.HIGHEST_PRECEDENCE)는 필터의 우선순위(적용 순서)
        // Ordered.HIGHEST_PRECEDENCE 는 가장 앞선순위를 말하는데 global 필터보다도 앞선다.

        return filter;
    }

    @Getter
    @Setter
    public static class Config {
        // 여기에 CustomFilter의 Configuration 정보를 넣는다.
        // 해당 정보는 application.yml 에서 초기화 한다.
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
