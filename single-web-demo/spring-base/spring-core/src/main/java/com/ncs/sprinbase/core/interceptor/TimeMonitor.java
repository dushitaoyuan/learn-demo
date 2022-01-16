package com.ncs.sprinbase.core.interceptor;

import java.lang.annotation.*;

/**
 * @author dushitaoyuan
 * @date 2019/10/12 14:20
 * @desc 时间监控注解
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeMonitor {
    boolean need() default true;
}


