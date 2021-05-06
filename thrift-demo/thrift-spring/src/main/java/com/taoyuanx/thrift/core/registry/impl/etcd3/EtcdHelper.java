package com.taoyuanx.thrift.core.registry.impl.etcd3;

import com.alibaba.fastjson.JSON;
import com.taoyuanx.thrift.core.registry.ServiceInfo;

/**
 * @author dushitaoyuan
 * @desc
 * @date 2021/5/6 05-06
 */
public class EtcdHelper {
    /**
     * 服务namespace
     */
    public static String ROOT_PATH = "services/";

    public static final String SPLIT = "/";

    /**
     * 超时时间
     */
    public static final long TIMEOUT_SECOND = 3L;


    public static String toServicePath(String serviceName) {
        return ROOT_PATH + serviceName + SPLIT;
    }

    public static String toServiceInstanceKey(ServiceInfo serviceInfo) {
        return toServicePath(serviceInfo.getServiceName()) + serviceInfo.getIp() + ":" + serviceInfo.getPort();
    }

    public static String toServiceValue(ServiceInfo serviceInfo) {
        return JSON.toJSONString(serviceInfo);
    }

    public static ServiceInfo valueToService(String serviceInfo) {
        return JSON.parseObject(serviceInfo, ServiceInfo.class);
    }

    public static String getServiceFromServiceInstanceKey(String serviceInstanceKey) {
        return serviceInstanceKey.split(SPLIT)[1];
    }


}
