package com.taoyuanx.demo.config.security;

import com.taoyuanx.demo.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginService loginService;

    @Autowired
    MyAuthenticationProcessingFilter myAuthenticationProcessingFilter;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login", "/css/**", "/img/**", "/js/**")
                .permitAll()
                .antMatchers("/**").hasAnyRole("admin")
                .anyRequest().authenticated()
                .and()
                .addFilterAt(myAuthenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successForwardUrl("/index")
                /* .successHandler(myAuthenticationSuccessHandler)
                 .failureHandler(myAuthenticationFailureHandler)*/
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .permitAll();
        http.csrf().disable().cors();

    }

}

