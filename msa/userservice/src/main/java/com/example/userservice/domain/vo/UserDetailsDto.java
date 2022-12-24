package com.example.userservice.domain.vo;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

    private String email;

    private String name;

    private String username;

    private Collection<GrantedAuthority> authorities;
}
