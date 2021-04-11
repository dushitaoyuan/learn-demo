package com.taoyuanx.demo.api.dto;

import io.airlift.drift.annotations.ThriftField;
import io.airlift.drift.annotations.ThriftStruct;

/**
 * @author dushitaoyuan
 * @date 2021/4/1116:23
 */
@ThriftStruct
public class HelloRequest {

    private String name;
    private String value;

    @ThriftField(1)
    public String getName() {
        return name;
    }

    @ThriftField
    public void setName(String name) {
        this.name = name;
    }

    @ThriftField(2)
    public String getValue() {
        return value;
    }
    @ThriftField
    public void setValue(String value) {
        this.value = value;
    }
}
