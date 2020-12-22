package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.service.LoginService;
import com.taoyuanx.demo.utils.PasswordUtil;
import com.taoyuanx.demo.vo.LoginUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

public class MyAuthenticationProvider implements AuthenticationProvider {


    private  LoginService loginService;

    public MyAuthenticationProvider(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
            String userName = usernamePasswordAuthenticationToken.getName();
            String credentials = (String) usernamePasswordAuthenticationToken.getCredentials();
            UserEntity user = loginService.findByUserName(userName);
            if (Objects.isNull(user)) {
                throw new UsernameNotFoundException("账号密码不匹配");
            }
            if (!PasswordUtil.passwordEqual(user.getPassword(), credentials)) {
                throw new BadCredentialsException("账号密码不匹配");
            }
            LoginUserVo loginUserVo = (LoginUserVo) loginService.loadUserByUsername(userName);
            return new UsernamePasswordAuthenticationToken(loginUserVo, loginUserVo.getPassword(), loginUserVo.getAuthorities());

        } else if (authentication instanceof RememberMeAuthenticationToken) {
            RememberMeAuthenticationToken rememberMeAuthenticationToken = (RememberMeAuthenticationToken) authentication;
            String userName = rememberMeAuthenticationToken.getName();
            UserEntity user = loginService.findByUserName(userName);
            if (Objects.isNull(user)) {
                throw new UsernameNotFoundException("账号密码不匹配");
            }
            LoginUserVo loginUserVo = (LoginUserVo) loginService.loadUserByUsername(userName);
            return new RememberMeAuthenticationToken(loginUserVo.getPassword(), loginUserVo, loginUserVo.getAuthorities());
        }
        throw new BadCredentialsException("登录失败");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
