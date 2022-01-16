package com.ncs.single.mvc.security;

import com.ncs.single.mvc.enums.RoleTypeEnum;

import java.lang.annotation.*;

/**
 * @author lianglei
 * @date 2019/10/12 14:20
 * @desc 简单权限控制
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    RoleTypeEnum[] role();
}
