package com.taoyuanx.thrift.core.registry.impl.etcd3;

import com.google.auto.service.AutoService;
import com.ibm.etcd.client.kv.WatchUpdate;
import com.taoyuanx.thrift.core.exception.MyThriftExceptioin;
import com.taoyuanx.thrift.core.registry.IServiceDiscovery;
import com.taoyuanx.thrift.core.registry.ServiceInfo;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;


/**
 * @author dushitaoyuan
 * @date 2021/5/411:33
 */

@Slf4j
@AutoService(IServiceDiscovery.class)
public class Etcd3ServiceDiscovery implements IServiceDiscovery {

    private static Map<String, List<ServiceInfo>> DISCOVER_SERVICE = new ConcurrentHashMap<>();
    private Etcd3ClientWrapper etcd3Client;


    private static volatile boolean inited = false;


    @Override
    public List<ServiceInfo> discoveryService(String serviceName, Consumer<List<ServiceInfo>> serviceInfoChangedListConsumer) {
        try {
            if (DISCOVER_SERVICE.containsKey(serviceName)) {
                return DISCOVER_SERVICE.get(serviceName);
            }
            String servicePath = EtcdHelper.toServicePath(serviceName);
            DISCOVER_SERVICE.computeIfAbsent(serviceName, (key) -> {
                List<ServiceInfo> serviceInfoList = etcd3Client.listByKeyPrefix(servicePath).stream()
                        .map(EtcdHelper::valueToService)
                        .collect(Collectors.toList());
                /**
                 * 监听服务列表变动
                 */
                etcd3Client.watchKeyPrefix(servicePath, buildWatchObserver(serviceName, serviceInfoChangedListConsumer));
                return serviceInfoList;
            });
            return DISCOVER_SERVICE.get(serviceName);
        } catch (Exception e) {
            throw new MyThriftExceptioin(e);
        }
    }

    @Override
    public void close() {
        try {
            Optional.ofNullable(etcd3Client).ifPresent(Etcd3ClientWrapper::close);
        } catch (Exception e) {
            log.warn("discovery close error", e);
        }
    }

    @Override
    public void init(String discoveryUrl) {
        if (!inited) {
            synchronized (this) {
                if (!inited) {
                    this.etcd3Client = new Etcd3ClientWrapper(discoveryUrl, true);
                    inited = true;
                }
            }

        }
    }


    private StreamObserver<WatchUpdate> buildWatchObserver(String serviceName, Consumer<List<ServiceInfo>> serviceInfoChangedListConsumer) {
        return new StreamObserver<WatchUpdate>() {

            @Override
            public void onNext(WatchUpdate watchUpdate) {

                if (!DISCOVER_SERVICE.containsKey(serviceName)) {
                    /**
                     * never  step
                     */
                    log.warn("serviceName nerver discovery", serviceName);
                    return;
                }
                List<ServiceInfo> addList = new ArrayList<>();
                List<String> deleteKeyList = new ArrayList<>();
                watchUpdate.getEvents().stream().forEach((event) -> {
                    switch (event.getType()) {
                        case PUT:
                            String putValue = event.getKv().getValue().toStringUtf8();
                            if (StringUtils.isNotEmpty(putValue)) {
                                ServiceInfo temp = EtcdHelper.valueToService(putValue);
                                if (temp.getServiceName().equals(serviceName)) {
                                    addList.add(temp);
                                }
                            }
                            break;
                        case DELETE:
                            String deleteKey = event.getKv().getValue().toStringUtf8();
                            if (StringUtils.isNotEmpty(deleteKey)) {
                                deleteKeyList.add(EtcdHelper.getServiceFromServiceInstanceKey(deleteKey));
                            }
                            break;
                        case UNRECOGNIZED:
                            log.warn("watchUpdate UNRECOGNIZED change type");
                            break;
                    }
                });
                /**
                 * 合并过滤数据
                 */
                Map<String, ServiceInfo> allServerInfoMap = new HashMap<>();
                List<ServiceInfo> oldServiceInfoList = DISCOVER_SERVICE.get(serviceName);
                oldServiceInfoList.stream().forEach(serviceInfo -> {
                    allServerInfoMap.put(buildServerInfoKey(serviceInfo), serviceInfo);
                });
                addList.stream().forEach(serviceInfo -> {
                    allServerInfoMap.put(buildServerInfoKey(serviceInfo), serviceInfo);
                });
                deleteKeyList.stream().forEach(deleteKey -> {
                    allServerInfoMap.remove(deleteKey);
                });
                /**
                 * 覆盖旧数据
                 */
                List<ServiceInfo> newList = new ArrayList<>(allServerInfoMap.values());
                DISCOVER_SERVICE.put(serviceName, newList);
                serviceInfoChangedListConsumer.accept(newList);
                log.info("serviceInfo changed {}->{}", oldServiceInfoList, newList);


            }

            @Override
            public void onError(Throwable t) {
                log.error("serviceList change watch error", t);
            }

            @Override
            public void onCompleted() {

            }
        };
    }

    private String buildServerInfoKey(ServiceInfo serviceInfo) {
        return serviceInfo.getIp() + ":" + serviceInfo.getPort();
    }


}
