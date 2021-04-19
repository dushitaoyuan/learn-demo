package com.taoyuanx.thrift.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ThriftImportSelector.class)
public @interface EnableThriftClient {
}
