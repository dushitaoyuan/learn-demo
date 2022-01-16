package com.ncs.sprinbase.core.log;

import java.lang.annotation.*;

/**
 * @author dushitaoyuan
 * @date 2019/11/4 18:14
 * @desc 操作日志 配合aop
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateAudit {

    // 操作代码
    int code();
}


