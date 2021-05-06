package com.taoyuanx.thrift.core.registry;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author dushitaoyuan
 * @date 2021/5/410:51
 * @desc: 服务注册
 */
public interface IServiceDiscovery {

    /**
     * 服务发现
     *
     * @param serviceName 服务名
     * @return
     */
    List<ServiceInfo> discoveryService(String serviceName, Consumer<List<ServiceInfo>> serviceInfoChangedListConsumer);


    void init(String discoverUrl);

    void close();
}
