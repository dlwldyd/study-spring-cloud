package com.example.userservice.security;

import com.example.userservice.domain.vo.JsonAuthenticationToken;
import com.example.userservice.domain.vo.MemberContext;
import com.example.userservice.domain.vo.UserDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Environment env;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = getJwtToken(request);
        if (jwt != null) {
            try {
                Map<String, Object> subject = parseSubject(jwt);
                UserDetailsDto userDetailsDto = objectMapper.convertValue(subject, UserDetailsDto.class);
                JsonAuthenticationToken jsonAuthenticationToken = new JsonAuthenticationToken(
                        new MemberContext(userDetailsDto,
                                true, true, true, true,
                                userDetailsDto.getAuthorities()),
                        userDetailsDto.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(jsonAuthenticationToken);
            } catch (RuntimeException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Map<String, Object> parseSubject(String jwt) {
        Claims body = Jwts.parserBuilder()
                .setSigningKey(env.getProperty("token.secret").getBytes(StandardCharsets.UTF_8))
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        // body.getSubject()로 하면 String으로 가져오는데 쌍따옴표가 없어서 objectMapper로 변환이 안됨
        Map<String, Object> subject = body.get("sub", LinkedHashMap.class);
        if (subject == null || subject.isEmpty()) {
            throw new RuntimeException("Unauthorized");
        }
        return subject;
    }

    private String getJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
