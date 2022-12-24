package com.example.userservice.domain.vo;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsVo {

    private String email;

    private String name;

    private String username;

    private Collection<GrantedAuthority> authorities;
}
