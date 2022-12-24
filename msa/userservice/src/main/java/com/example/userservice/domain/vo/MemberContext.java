package com.example.userservice.domain.vo;

import com.example.userservice.domain.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class MemberContext extends User {

    private final String email;

    private final String name;

    private final String username;

    private String encryptedPwd;

    public MemberContext(Member member, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(member.getUsername(), "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = member.getEmail();
        this.name = member.getName();
        this.username = member.getUsername();
        this.encryptedPwd = member.getEncryptedPwd();
    }

    public MemberContext(UserDetailsDto userDetailsDto, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(userDetailsDto.getUsername(), "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.email = userDetailsDto.getEmail();
        this.name = userDetailsDto.getName();
        this.username = userDetailsDto.getUsername();
        this.encryptedPwd = null;
    }
}
