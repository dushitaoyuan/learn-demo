package com.taoyuanx.demo.utils;

import com.taoyuanx.demo.dto.LoginForm;
import org.springframework.security.authentication.AuthenticationServiceException;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Objects;

/**
 * @author dushitaoyuan
 * @date 2020/12/22
 */
public class RequestUtil {
    public static String LOGIN_FORM_KEY = "loginForm";

    /**
     * 解析 springsecurity 登录请求
     */
    public static LoginForm readRequest(HttpServletRequest request) {
        try {
            Object attribute = request.getAttribute(LOGIN_FORM_KEY);
            if (Objects.isNull(attribute)) {
                if (request.getContentType() == null || !request.getContentType().contains("json")) {
                    throw new AuthenticationServiceException("请求头类型不支持: " + request.getContentType());
                }
                InputStream inputStream = request.getInputStream();
                LoginForm loginForm = JSONUtil.getObjectMapper()
                        .readValue(inputStream, LoginForm.class);
                /**
                 * 表单校验
                 */
                request.setAttribute(LOGIN_FORM_KEY, loginForm);
                return loginForm;
            }
            return (LoginForm) attribute;
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }

    }
}
