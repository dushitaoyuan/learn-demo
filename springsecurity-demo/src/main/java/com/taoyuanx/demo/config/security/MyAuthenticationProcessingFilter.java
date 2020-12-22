package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.utils.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class MyAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {



    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return RequestUtil.readRequest(request).getPassword();
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return RequestUtil.readRequest(request).getUsername();
    }


}
