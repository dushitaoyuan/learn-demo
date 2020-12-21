package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.service.LoginService;
import com.taoyuanx.demo.utils.PasswordUtil;
import com.taoyuanx.demo.vo.LoginUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    LoginService loginService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String credentials = (String) authentication.getCredentials();
        UserEntity user = loginService.findByUserName(userName);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("账号密码不匹配");
        }
        if (!PasswordUtil.passwordEqual(user.getPassword(), credentials)) {
            throw new BadCredentialsException("账号密码不匹配");
        }
        LoginUserVo loginUserVo = (LoginUserVo) loginService.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(loginUserVo, loginUserVo.getPassword(), loginUserVo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
