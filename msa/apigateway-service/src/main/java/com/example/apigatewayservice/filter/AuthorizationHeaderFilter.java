package com.example.apigatewayservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private final Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        // 필수
        super(Config.class);
        this.env = env;
    }

    @Override
    public GatewayFilter apply(Config config) {

        // pre filter
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer ", "");

            if (!isValid(jwt)) {
                return onError(exchange, "Token is not valid", HttpStatus.UNAUTHORIZED);
            }

            // pre filter에 chaining 으로 post filter 가 추가됨
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }

    // 인증 실패 시 마이크로서비스로 포워드 하지 않고 Unauthorized로 응답
    private Mono<Void> onError(ServerWebExchange exchange, String errMessage, HttpStatus httpStatus) {

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(errMessage);

        return response.setComplete();
    }

    private boolean isValid(String jwt) {

        boolean result = true;

        String subject = null;

        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8))
                    .build()
                    // jwt의 종류로는 jws 와 jwe가 있는데 jws는 해시 알고리즘을 통해 서명한것 이다. 즉, 우리가 일반적으로 jwt라 일컬을 때 jws를 말한다.
                    // jwe 는 데이터를 암호화한 후 해시 알고리즘을 통해 서명하여 데이터 유출을 방지한 것이다.
                    .parseClaimsJws(jwt)
//                .getHeader()
//                .getSignature()
                    .getBody();
            subject = body.getSubject();
        } catch (Exception e) {
            result = false;
        }

        if (subject == null || subject.isEmpty()) {
            result = false;
        }

        return result;
    }
}
