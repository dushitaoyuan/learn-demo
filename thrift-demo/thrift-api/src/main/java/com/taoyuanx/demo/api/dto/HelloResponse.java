package com.taoyuanx.demo.api.dto;

import io.airlift.drift.annotations.ThriftField;
import io.airlift.drift.annotations.ThriftStruct;

/**
 * @author dushitaoyuan
 * @date 2021/4/1116:23
 */
@ThriftStruct
public class HelloResponse {
    private String msg;

    @ThriftField(1)
    public String getMsg() {
        return msg;
    }

    @ThriftField
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
