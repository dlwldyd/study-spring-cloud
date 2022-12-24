package com.example.userservice.domain.vo;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.util.Assert;

import java.util.Collection;

@Getter
public class JsonAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final MemberContext memberContext;

    public JsonAuthenticationToken(MemberContext memberContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.memberContext = memberContext;
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() {
        return this.memberContext.getEncryptedPwd();
    }

    @Override
    public Object getPrincipal() {
        return this.memberContext.getUsername();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
                "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.memberContext.setEncryptedPwd(null);
    }
}
