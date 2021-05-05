package com.taoyuanx.thrift.core.registry;

import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2021/5/410:51
 * @desc: 服务注册
 */
public interface IServiceDiscovery {

    /**
     * 服务注册
     *
     * @param serviceInfo 服务信息
     * @return
     */
    boolean registerService(ServiceInfo serviceInfo);

    /**
     * 服务发现
     *
     * @param serviceName 服务名
     * @return
     */
    List<ServiceInfo> discoveryService(String serviceName);


    boolean unRegister(ServiceInfo serviceInfo);




    void close();


    void init(String url);


}
