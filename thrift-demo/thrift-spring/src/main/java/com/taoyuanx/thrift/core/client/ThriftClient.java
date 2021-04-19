package com.taoyuanx.thrift.core.client;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dushitaoyuan
 * @date 2021/4/1821:04
 * @desc: thrift client标记
 */

@Component
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThriftClient {

    String serviceName() default "";

    String[] ip() default {};

    int port() default 9090;

    double version() default 1.0d;
}
