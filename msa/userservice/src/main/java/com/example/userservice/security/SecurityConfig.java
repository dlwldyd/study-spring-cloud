package com.example.userservice.security;

import com.example.userservice.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final Environment env;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();
//        http.authorizeHttpRequests().antMatchers("/**").permitAll();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 생성 안함
        http.authorizeHttpRequests()
                .antMatchers(HttpMethod.GET, "/users").authenticated()
                .antMatchers("/**").permitAll();
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new JsonAuthenticationProvider(userService, passwordEncoder);
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        authenticationFilter.setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            long expiration = Long.parseLong(Objects.requireNonNull(env.getProperty("token.expiration-time")));

            // 서명 알고리즘에 넣을 키 값 생성
            SecretKey key = Keys.hmacShaKeyFor(Objects.requireNonNull(env.getProperty("token.secret")).getBytes(StandardCharsets.UTF_8));

            // jwt 토큰 생성
            String jwt = Jwts.builder()
                    .setHeaderParam("typ", "JWT") // 헤더에 typ라는 키로 값을 저장
                    .claim("sub", authentication.getPrincipal()) // claim에 sub라는 키로 값을 저장
//                    .setSubject((String) authentication.getPrincipal()) // 위와 같이 claim에 sub라는 키로 값을 저장
                    .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 토큰 만료 시간
                    .signWith(key, SignatureAlgorithm.HS512) // 서명 알고리즘
                    .compact();

            response.addHeader("jwt", jwt);
        });
        authenticationFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        });
        return authenticationFilter;
    }
}
