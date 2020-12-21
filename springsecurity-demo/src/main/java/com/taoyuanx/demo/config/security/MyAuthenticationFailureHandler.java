package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.api.Result;
import com.taoyuanx.demo.api.ResultBuilder;
import com.taoyuanx.demo.utils.JSONUtil;
import com.taoyuanx.demo.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Result result = null;
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            result = ResultBuilder.failed(e.getMessage());
        } else if (e instanceof LockedException) {
            result = ResultBuilder.failed("账户被锁定，请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            result = ResultBuilder.failed("密码过期，请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            result = ResultBuilder.failed("账户过期，请联系管理员!");
        } else if (e instanceof DisabledException) {
            result = ResultBuilder.failed("账户被禁用，请联系管理员!");
        } else {
            log.error("登录失败", e);
            result = ResultBuilder.failed("登录失败"+e.getMessage());
        }
        ResponseUtil.responseJson(response, JSONUtil.toJsonString(result), 200);
    }
}