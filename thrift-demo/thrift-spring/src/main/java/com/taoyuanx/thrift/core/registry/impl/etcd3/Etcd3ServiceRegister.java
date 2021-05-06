package com.taoyuanx.thrift.core.registry.impl.etcd3;

import com.google.auto.service.AutoService;
import com.taoyuanx.thrift.core.exception.MyThriftExceptioin;
import com.taoyuanx.thrift.core.registry.IServiceRegister;
import com.taoyuanx.thrift.core.registry.ServiceInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2021/5/411:33
 */

@Slf4j
@AutoService(IServiceRegister.class)
public class Etcd3ServiceRegister implements IServiceRegister {
    private Etcd3ClientWrapper etcd3Client;


    private static volatile boolean inited = false;


    @Override
    public void registerService(ServiceInfo serviceInfo) {
        try {
            etcd3Client.putEphemeral(EtcdHelper.toServiceInstanceKey(serviceInfo), EtcdHelper.toServiceValue(serviceInfo));
            log.info("register {} success", serviceInfo);
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }

    @Override
    public void registerService(List<ServiceInfo> serviceInfoList) {
        try {
            Map<String, String> serviceInfoMap = new HashMap<>();
            serviceInfoList.forEach(serviceInfo -> {
                serviceInfoMap.put(EtcdHelper.toServiceInstanceKey(serviceInfo), EtcdHelper.toServiceValue(serviceInfo));
            });
            etcd3Client.putEphemeral(serviceInfoMap);
            log.info("batch register {} success", serviceInfoMap);
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }


    @Override
    public void unRegister(ServiceInfo serviceInfo) {
        try {
            etcd3Client.delete(EtcdHelper.toServiceInstanceKey(serviceInfo));
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }


    @Override
    public void close() {
        try {
            Optional.ofNullable(etcd3Client).ifPresent(Etcd3ClientWrapper::close);
        } catch (Exception e) {
            log.warn("register close error", e);
        }
    }

    @Override
    public void init(String registerUrl) {
        if (!inited) {
            synchronized (this) {
                if (!inited) {
                    this.etcd3Client = new Etcd3ClientWrapper(registerUrl, true);
                    inited = true;
                }
            }

        }
    }


}
