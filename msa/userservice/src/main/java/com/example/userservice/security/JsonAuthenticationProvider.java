package com.example.userservice.security;

import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@RequiredArgsConstructor
public class JsonAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //authentication 에는 사용자가 입력한 아이디, 패스워드 정보가 들어있음
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad credential");
        }

        //인자가 2개인(권한 정보X) 생정자는 로그인 시도 시 사용되는 생성자이다.
        //인자가 3개인(권한 정보O) 생성자는 로그인 성공 시 인증 토큰을 만들기 위해 사용되는 생성자이다.
        //인자 : principal -> 사용자 정보, credential -> 패스워드 정보, authorities -> 권한 정보
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
