package com.taoyuanx.demo.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MyAuthenticationManager implements AuthenticationManager {

    private final MyAuthenticationProvider myAuthenticationProvider;

    public MyAuthenticationManager(MyAuthenticationProvider myAuthenticationProvider) {
        this.myAuthenticationProvider = myAuthenticationProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = myAuthenticationProvider.authenticate(authentication);
        if (Objects.nonNull(result)) {
            return result;
        }
        throw new ProviderNotFoundException("Authentication failed!");
    }
}