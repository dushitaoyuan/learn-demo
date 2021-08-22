package com.taoyuanx.thrift.core.server;


import com.taoyuanx.thrift.core.ThriftConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dushitaoyuan
 * @date 2021/4/1821:04
 * @desc: thriftService标记
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ThriftServiceImpl {

    int port() default ThriftConstant.PORT;

    int requestTimeOut() default 3;

    double version() default 1.0d;

    int warmupTime() default ThriftConstant.DEFAULT_WARMUP;

    Class serviceInterface() default Object.class;


}
