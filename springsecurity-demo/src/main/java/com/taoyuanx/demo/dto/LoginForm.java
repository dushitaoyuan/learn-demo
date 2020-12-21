package com.taoyuanx.demo.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @date 2020/9/17
 */
@Data
public class LoginForm implements Serializable {
    private String username;
    private String password;
    /**
     * 记住我登录
     */
    private boolean rememberMe;
}
