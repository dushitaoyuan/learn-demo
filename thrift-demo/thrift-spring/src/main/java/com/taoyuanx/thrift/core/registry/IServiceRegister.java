package com.taoyuanx.thrift.core.registry;


import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2021/5/410:51
 * @desc: 服务注册
 */
public interface IServiceRegister {

    /**
     * 服务注册
     *
     * @param serviceInfo 服务信息
     * @return
     */
    void registerService(ServiceInfo serviceInfo);

    void registerService(List<ServiceInfo> serviceInfoList);

    /**
     * 服务下线
     *
     * @param serviceInfo
     * @return
     */
    void unRegister(ServiceInfo serviceInfo);


    void init(String registerUrl);

    void close();


}
