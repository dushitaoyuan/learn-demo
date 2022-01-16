package com.ncs.single.mvc.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author dushitaoyuan
 * @desc 登录表单
 * @date 2019/12/17
 */
@Data
public class LoginForm {
    private String phone;
    private String email;
    private String password;
    private String username;
    //登录类型
    @NotNull(message = "登录类型不可为空")
    private Integer loginType;
    //验证码
    @NotBlank(message = "验证码不为空")
    private String vafyCode;
}
