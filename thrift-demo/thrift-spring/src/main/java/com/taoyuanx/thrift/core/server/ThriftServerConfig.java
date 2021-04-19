package com.taoyuanx.thrift.core.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import io.airlift.drift.codec.ThriftCodecManager;
import io.airlift.drift.server.DriftServer;
import io.airlift.drift.server.DriftService;
import io.airlift.drift.server.stats.NullMethodInvocationStatsFactory;
import io.airlift.drift.transport.netty.server.DriftNettyServerConfig;
import io.airlift.drift.transport.netty.server.DriftNettyServerTransportFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2021/4/1821:17
 * @desc: 处理bean注解
 */
public class ThriftServerConfig implements ApplicationContextAware, InitializingBean, DisposableBean {
    private ApplicationContext applicationContext;

    private List<DriftServer> driftServerList;

    @Override
    public void destroy() throws Exception {
        if (!CollectionUtils.isEmpty(driftServerList)) {
            driftServerList.stream().forEach(DriftServer::shutdown);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ThriftServiceImpl.class);
        Map<Integer, List<ThriftServer>> portServerMap = beansWithAnnotation.values().stream().map((serviceBean) -> {
            ThriftServiceImpl annotation = AnnotationUtils.findAnnotation(serviceBean.getClass(), ThriftServiceImpl.class);
            ThriftServer thriftServer = new ThriftServer();
            thriftServer.setPort(annotation.port());
            thriftServer.setServiceImpl(serviceBean);
            thriftServer.setRequestTimeout(annotation.timeOut());
            return thriftServer;
        }).collect(Collectors.groupingBy(ThriftServer::getPort));
        driftServerList = portServerMap.entrySet().stream().map(kv -> {
            return mapToDriftServer(kv.getValue(), kv.getKey());
        }).collect(Collectors.toList());
        driftServerList.stream().forEach(DriftServer::start);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private DriftServer mapToDriftServer(List<ThriftServer> thriftServerList, int port) {
        List<DriftService> driftServiceList = Lists.newArrayListWithCapacity(thriftServerList.size());
        int maxWorkThreadNum = 0, maxIoThreadNum = 0;
        for (ThriftServer thriftServer : thriftServerList) {
            driftServiceList.add(new DriftService(thriftServer.getServiceImpl()));
            maxIoThreadNum = Math.max(maxWorkThreadNum, thriftServer.getIoThreadNum());
            maxWorkThreadNum = Math.max(maxWorkThreadNum, thriftServer.getWorkThreadNum());

        }
        DriftNettyServerConfig serverConfig = new DriftNettyServerConfig();
        serverConfig.setPort(port);
        if (maxWorkThreadNum > 0) {
            serverConfig.setWorkerThreadCount(maxWorkThreadNum);
        }
        if (maxIoThreadNum > 0) {
            serverConfig.setIoThreadCount(maxIoThreadNum);
        }
        return new DriftServer(
                new DriftNettyServerTransportFactory(serverConfig),
                new ThriftCodecManager(),
                new NullMethodInvocationStatsFactory(),
                ImmutableSet.copyOf(driftServiceList),
                ImmutableSet.of());
    }
}
