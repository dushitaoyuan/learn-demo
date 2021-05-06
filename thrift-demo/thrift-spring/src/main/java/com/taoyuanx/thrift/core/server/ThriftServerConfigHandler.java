package com.taoyuanx.thrift.core.server;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.taoyuanx.thrift.core.ThriftConstant;
import com.taoyuanx.thrift.core.registry.IServiceDiscovery;
import com.taoyuanx.thrift.core.registry.IServiceRegister;
import com.taoyuanx.thrift.core.registry.ServiceInfo;
import com.taoyuanx.thrift.core.util.IpUtil;
import com.taoyuanx.thrift.core.util.ServiceUtil;
import io.airlift.drift.codec.ThriftCodecManager;
import io.airlift.drift.server.DriftServer;
import io.airlift.drift.server.DriftService;
import io.airlift.drift.server.stats.NullMethodInvocationStatsFactory;
import io.airlift.drift.transport.netty.server.DriftNettyServerConfig;
import io.airlift.drift.transport.netty.server.DriftNettyServerTransportFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2021/4/1821:17
 * @desc: 处理bean注解
 */
@Slf4j
public class ThriftServerConfigHandler implements ApplicationContextAware, InitializingBean, DisposableBean {
    private ApplicationContext applicationContext;

    private List<DriftServer> driftServerList;
    private Map<String, ServiceInfo> serviceInfoMap = new ConcurrentHashMap<>();
    private static IServiceRegister serviceRegister = ServiceUtil.loadSingleService(IServiceRegister.class);

    @Override
    public void destroy() throws Exception {
        if (!CollectionUtils.isEmpty(driftServerList)) {
            driftServerList.stream().forEach(DriftServer::shutdown);
        }
        if (Objects.nonNull(serviceRegister)) {
            serviceInfoMap.values().stream().forEach(serviceRegister::unRegister);
            serviceRegister.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ThriftServiceImpl.class);

        Map<Class, Object> serviceImplMap = new HashMap<>();

        Map<Integer, List<ThriftServerConfig>> portServerMap = beansWithAnnotation.values().stream().filter(serviceBean -> {
            boolean isImplInterface = AopUtils.getTargetClass(serviceBean).getInterfaces().length > 0;
            ThriftServiceImpl annotation = AnnotationUtils.findAnnotation(serviceBean.getClass(), ThriftServiceImpl.class);
            if (!isImplInterface) {
                log.warn("@ThriftServiceImpl must impl a interface");
                return false;
            }
            Class interfaceClass = annotation.serviceInterface();
            if (interfaceClass.equals(Object.class)) {
                interfaceClass = AopUtils.getTargetClass(serviceBean).getInterfaces()[0];
            }
            if (serviceImplMap.containsKey(interfaceClass)) {
                log.warn("{} has multiple implementations", interfaceClass);
                return false;
            }
            serviceImplMap.put(interfaceClass, serviceBean);
            return true;
        }).map(serviceBean -> {
            ThriftServiceImpl annotation = AnnotationUtils.findAnnotation(serviceBean.getClass(), ThriftServiceImpl.class);
            ThriftServerConfig thriftServerConfig = new ThriftServerConfig();
            thriftServerConfig.setPort(annotation.port());
            thriftServerConfig.setServiceImpl(serviceBean);
            Class interfaceClass = annotation.serviceInterface();
            if (interfaceClass.equals(Object.class)) {
                interfaceClass = AopUtils.getTargetClass(serviceBean).getInterfaces()[0];
            }
            thriftServerConfig.setServiceInterface(interfaceClass.getName());
            thriftServerConfig.setServiceInterfaceClass(interfaceClass);
            thriftServerConfig.setRequestTimeout(annotation.requestTimeOut());
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setWeight(thriftServerConfig.getWeight());
            serviceInfo.setServiceName(thriftServerConfig.getServiceInterface());
            serviceInfo.setPort(thriftServerConfig.getPort());
            serviceInfo.setIp(IpUtil.getNetAddress(thriftServerConfig.getNetPrefix()));
            serviceInfo.setPort(thriftServerConfig.getPort());
            serviceInfo.setTimestamp(System.currentTimeMillis());
            serviceInfoMap.put(serviceInfo.getServiceName(), serviceInfo);
            return thriftServerConfig;
        }).collect(Collectors.groupingBy(ThriftServerConfig::getPort));

        driftServerList = portServerMap.entrySet().stream().map(kv -> {
            return mapToDriftServer(kv.getValue(), kv.getKey());
        }).collect(Collectors.toList());
        driftServerList.stream().forEach(DriftServer::start);
        //服务注册
        serviceRegister.registerService(serviceInfoMap.values().stream().collect(Collectors.toList()));


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        serviceRegister = ServiceUtil.loadSingleService(IServiceRegister.class);
        if (Objects.nonNull(serviceRegister)) {
            serviceRegister.init(applicationContext.getEnvironment().getProperty(ThriftConstant.SERVICE_REGISTER_URL));
        }


    }

    private DriftServer mapToDriftServer(List<ThriftServerConfig> ThriftServerConfigList, int port) {
        List<DriftService> driftServiceList = Lists.newArrayListWithCapacity(ThriftServerConfigList.size());
        int maxWorkThreadNum = 0, maxIoThreadNum = 0;
        for (ThriftServerConfig ThriftServerConfig : ThriftServerConfigList) {
            driftServiceList.add(new DriftService(ThriftServerConfig.getServiceImpl()));
            maxIoThreadNum = Math.max(maxWorkThreadNum, ThriftServerConfig.getIoThreadNum());
            maxWorkThreadNum = Math.max(maxWorkThreadNum, ThriftServerConfig.getWorkThreadNum());

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
