package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.api.ResultBuilder;
import com.taoyuanx.demo.utils.JSONUtil;
import com.taoyuanx.demo.utils.ResponseUtil;
import com.taoyuanx.demo.vo.LoginUserVo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        LoginUserVo loginUserVo = ((LoginUserVo) auth.getPrincipal());
        ResponseUtil.responseJson(response, JSONUtil.toJsonString(ResultBuilder.success("登录成功")), 200);
    }
}