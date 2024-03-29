package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.api.ResultBuilder;
import com.taoyuanx.demo.utils.JSONUtil;
import com.taoyuanx.demo.utils.ResponseUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (ResponseUtil.isAcceptJson(request)) {
            ResponseUtil.responseJson(response, JSONUtil.toJsonString(ResultBuilder.success("退出成功")), 200);
        } else {
            response.sendRedirect("/login");
        }
        System.out.println("注销成功!");
    }
}
