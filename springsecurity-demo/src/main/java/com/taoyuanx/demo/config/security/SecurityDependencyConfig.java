package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.dto.LoginForm;
import com.taoyuanx.demo.service.LoginService;
import com.taoyuanx.demo.utils.RequestUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 *
 */
@Configuration
@Data
public class SecurityDependencyConfig {
    @Value(value = "${spring.security.rememberKey}")
    private String rememberKey;



    @Bean
    public RememberMeServices rememberMeServices(LoginService loginService) {
        return new TokenBasedRememberMeServices(rememberKey, loginService) {
            @Override
            protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
                LoginForm loginForm = RequestUtil.readRequest(request);
                return Objects.nonNull(loginForm) && loginForm.isRememberMe();
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(LoginService loginService) {
        return new MyAuthenticationProvider(loginService);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new MyAuthenticationManager(authenticationProvider);
    }
    @Bean
    public MyAuthenticationProcessingFilter authenticationProcessingFilter(AuthenticationManager authenticationManager,
                                                                           RememberMeServices rememberMeServices) {
        MyAuthenticationProcessingFilter authenticationProcessingFilter = new MyAuthenticationProcessingFilter();
        authenticationProcessingFilter.setRememberMeServices(rememberMeServices);
        authenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        authenticationProcessingFilter.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        authenticationProcessingFilter.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        authenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        return authenticationProcessingFilter;

    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new MyLogoutSuccessHandler();
    }


}

