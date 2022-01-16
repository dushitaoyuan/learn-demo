package com.ncs.single.mvc.vo;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author dushitaoyuan
 * @desc 注册表单
 * @date 2019/12/18
 */
@Data
public class RegistForm {
    private String phone;
    private String email;
    @NotBlank(message = "密码不可为空")
    private String password;
    @NotBlank(message = "确认密码不可为空")
    private String confirmPassword;
    private String username;
    //注册类型
    @NotNull(message = "注册类型不可为空")
    private Integer registType;
    //验证码
    @NotBlank(message = "验证码不为空")
    private String vafyCode;
}
