package com.mfa.otp;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class OtpAuthentication extends AbstractAuthenticationToken {
    // user id
    private String principal;
    // token associated with the user
    private String token;

    public OtpAuthentication(Collection<? extends GrantedAuthority> authorities, String principal, String token) {
        super(authorities);
        this.principal = principal;
        this.token = token;
    }

    public OtpAuthentication(String principal, String token) {
        super(List.of());
        this.principal = principal;
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
