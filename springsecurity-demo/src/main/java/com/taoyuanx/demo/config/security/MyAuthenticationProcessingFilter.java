package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.dto.LoginForm;
import com.taoyuanx.demo.exception.ServiceException;
import com.taoyuanx.demo.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
@Component
public class MyAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {


    public MyAuthenticationProcessingFilter(MyAuthenticationManager myAuthenticationManager,
                                            MyAuthenticationSuccessHandler myAuthenticationSuccessHandler,
                                            MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.setAuthenticationManager(myAuthenticationManager);
        this.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        this.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType() == null || !request.getContentType().contains("json")) {
            throw new AuthenticationServiceException("请求头类型不支持: " + request.getContentType());
        }
        Authentication authRequest;
        try {
            InputStream inputStream = request.getInputStream();
            LoginForm loginForm = JSONUtil.getObjectMapper()
                    .readValue(inputStream, LoginForm.class);
            if (Objects.isNull(loginForm)) {
                throw new ServiceException("请求异常");
            }
            if (loginForm.isRememberMe()) {
                authRequest = new RememberMeAuthenticationToken(loginForm.getUsername(), loginForm.getPassword(), null);
            } else {
                authRequest = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword(), null);

            }
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
