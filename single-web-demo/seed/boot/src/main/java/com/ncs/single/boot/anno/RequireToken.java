package com.ncs.single.boot.anno;


import java.lang.annotation.*;

/**
 * @author lianglei
 * @date 2019/10/12 14:20
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireToken {
    boolean require() default true;
}
