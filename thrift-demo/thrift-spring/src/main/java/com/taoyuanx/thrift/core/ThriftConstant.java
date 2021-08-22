package com.taoyuanx.thrift.core;

/**
 * @author dushitaoyuan
 * @date 2021/4/1822:30
 */
public class ThriftConstant {
    public static final int PORT = 9090;

    public static final int WEIGHT = 10;

    /**
     * 服务注册地址
     */
    public static final String SERVICE_REGISTER_URL = "thrift.service.register.url";

    /**
     * 服务发现地址
     */
    public static final String SERVICE_DISCOVERY_URL = "thrift.service.discovery.url";

    /**
     * 预热时间
     */
    public static final int DEFAULT_WARMUP = 10 * 60 * 1000;

    public static final String CONTEXT_KEY = "_context";

}
