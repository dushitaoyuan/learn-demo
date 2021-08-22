package com.taoyuanx.thrift.core.client;

import com.google.common.collect.ImmutableList;
import com.google.common.net.HostAndPort;
import com.taoyuanx.thrift.core.ThriftConstant;
import com.taoyuanx.thrift.core.exception.MyThriftExceptioin;
import com.taoyuanx.thrift.core.loadbalance.AbstractSelector;
import com.taoyuanx.thrift.core.loadbalance.RoundSelector;
import com.taoyuanx.thrift.core.loadbalance.ThriftServer;
import com.taoyuanx.thrift.core.registry.IServiceDiscovery;
import com.taoyuanx.thrift.core.registry.ServiceInfo;
import com.taoyuanx.thrift.core.util.ServiceUtil;
import io.airlift.drift.client.DriftClient;
import io.airlift.drift.client.DriftClientFactory;
import io.airlift.drift.codec.ThriftCodecManager;
import io.airlift.drift.transport.client.DriftClientConfig;
import io.airlift.drift.transport.netty.client.DriftNettyClientConfig;
import io.airlift.drift.transport.netty.client.DriftNettyMethodInvokerFactory;
import io.airlift.units.Duration;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ClientProxyFactory {


    private static Map<Class, ProxyObjectPair> PROXY_MAP = new ConcurrentHashMap<>();

    private ThriftCodecManager codecManager = new ThriftCodecManager();
    private static IServiceDiscovery serviceDiscovery = ServiceUtil.loadSingleService(IServiceDiscovery.class);


    private static ClientProxyFactory INSTANCE = new ClientProxyFactory();

    private ClientProxyFactory() {

    }

    public static ClientProxyFactory getInstance() {
        return INSTANCE;
    }

    public void init(String discoveryUrl) {
        serviceDiscovery.init(discoveryUrl);
    }


    public <T> T getServiceProxy(ThriftClientConfig thriftClientConfig) {
        Class serviceInterfaceClass = thriftClientConfig.getServiceInterfaceClass();
        if (!PROXY_MAP.containsKey(serviceInterfaceClass)) {
            AbstractSelector addressSelector = new RoundSelector();
            if (Objects.nonNull(thriftClientConfig.getServerList()) && thriftClientConfig.getServerList().length > 0) {
                List<HostAndPort> hostAndPortList = Arrays.stream(thriftClientConfig.getServerList())
                        .map(HostAndPort::fromString).collect(Collectors.toList());
                List<ThriftServer> staticServerList = hostAndPortList.stream().map(hostAndPort -> {
                    ThriftServer thriftServer = new ThriftServer();
                    thriftServer.setWeight(ThriftConstant.WEIGHT);
                    thriftServer.setHostAndPort(hostAndPort);
                    return thriftServer;
                }).collect(Collectors.toList());
                addressSelector.setServerList(staticServerList);
            } else {
                List<ThriftServer> discoveryServiceList = serviceDiscovery.discoveryService(thriftClientConfig.getServiceInterfaceName(),
                        (newServiceInfoList) -> {
                            addressSelector.setServerList(newServiceInfoList.stream().map(this::mapToThriftServer).collect(Collectors.toList()));
                        }).stream().map(this::mapToThriftServer).collect(Collectors.toList());
                addressSelector.setServerList(discoveryServiceList);
            }
            DriftNettyMethodInvokerFactory<?> methodInvokerFactory = DriftNettyMethodInvokerFactory
                    .createStaticDriftNettyMethodInvokerFactory(toDriftNettyClientConfig(thriftClientConfig));
            DriftClient<T> driftClient = new DriftClientFactory(codecManager, methodInvokerFactory, addressSelector)
                    .createDriftClient(serviceInterfaceClass, Optional.empty(), ImmutableList.of(new ClientContextFilter()), new DriftClientConfig());
            ProxyObjectPair proxyObjectPair = new ProxyObjectPair();
            proxyObjectPair.proxy = driftClient.get();
            proxyObjectPair.selector = addressSelector;
            proxyObjectPair.methodInvokerFactory = methodInvokerFactory;
            proxyObjectPair.thriftClientConfig = thriftClientConfig;
            PROXY_MAP.put(serviceInterfaceClass, proxyObjectPair);
        }
        return (T) PROXY_MAP.get(serviceInterfaceClass).getProxy();
    }

    public ThriftClientConfig buildThriftClientConfig(ThriftClient thriftClient, Class serviceInterfaceClass) {
        try {
            ThriftClientConfig clientConfig = new ThriftClientConfig();
            clientConfig.setRequestTimeout(thriftClient.requestTimeOut());
            clientConfig.setServiceInterfaceName(serviceInterfaceClass.getName());
            clientConfig.setServiceInterfaceClass(serviceInterfaceClass);
            clientConfig.setServerList(thriftClient.serverList());
            clientConfig.setConnectTimeout(thriftClient.requestTimeOut());
            return clientConfig;
        } catch (Exception e) {
            throw new MyThriftExceptioin("配置异常", e);
        }
    }

    private static class ProxyObjectPair {
        private Object proxy;
        private DriftNettyMethodInvokerFactory methodInvokerFactory;
        private ThriftClientConfig thriftClientConfig;
        private AbstractSelector selector;

        public Object getProxy() {
            return proxy;
        }

        public void setProxy(Object proxy) {
            this.proxy = proxy;
        }

        public DriftNettyMethodInvokerFactory getMethodInvokerFactory() {
            return methodInvokerFactory;
        }

        public void setMethodInvokerFactory(DriftNettyMethodInvokerFactory methodInvokerFactory) {
            this.methodInvokerFactory = methodInvokerFactory;
        }

        public ThriftClientConfig getThriftClientConfig() {
            return thriftClientConfig;
        }

        public void setThriftClientConfig(ThriftClientConfig thriftClientConfig) {
            this.thriftClientConfig = thriftClientConfig;
        }

        public AbstractSelector getSelector() {
            return selector;
        }

        public void setSelector(AbstractSelector selector) {
            this.selector = selector;
        }
    }

    private DriftNettyClientConfig toDriftNettyClientConfig(ThriftClientConfig clientConfig) {
        DriftNettyClientConfig driftNettyClientConfig = new DriftNettyClientConfig();
        if (Objects.nonNull(clientConfig.getCiphers())) {
            driftNettyClientConfig.setCiphers(clientConfig.getCiphers());
        }
        if (Objects.nonNull(clientConfig.getConnectTimeout()) && clientConfig.getConnectTimeout() > 0) {
            driftNettyClientConfig.setConnectTimeout(new Duration(clientConfig.getConnectTimeout(), SECONDS));
        }
        if (Objects.nonNull(clientConfig.getKey())) {
            File fileKey = new File(clientConfig.getKey());
            if (!fileKey.exists()) {
                throw new MyThriftExceptioin("key file not exists");
            }
            driftNettyClientConfig.setKey(fileKey);
        }
        if (Objects.nonNull(clientConfig.getKeyPassword())) {
            driftNettyClientConfig.setKeyPassword(clientConfig.getKeyPassword());
        }
        driftNettyClientConfig.setSslEnabled(clientConfig.isSslEnabled());
        if (Objects.nonNull(clientConfig.getTrustCertificate())) {
            File file = new File(clientConfig.getTrustCertificate());
            if (!file.exists()) {
                throw new MyThriftExceptioin("trustCertificate file not exists");
            }
            driftNettyClientConfig.setTrustCertificate(file);
        }
        driftNettyClientConfig.setProtocol(clientConfig.getProtocol());
        driftNettyClientConfig.setTransport(clientConfig.getTransport());
        return driftNettyClientConfig;
    }

    public void close() {
        if (Objects.nonNull(PROXY_MAP)) {
            PROXY_MAP.values().stream().forEach(proxyObjectPair -> {
                proxyObjectPair.getMethodInvokerFactory().close();
            });
            PROXY_MAP.clear();
        }
        if (Objects.nonNull(serviceDiscovery)) {
            serviceDiscovery.close();
        }
    }

    public void close(Class interfaceClass) {
        if (PROXY_MAP.containsKey(interfaceClass)) {
            PROXY_MAP.get(interfaceClass).getMethodInvokerFactory().close();
            PROXY_MAP.remove(interfaceClass);
        }

    }

    private ThriftServer mapToThriftServer(ServiceInfo serviceInfo) {
        ThriftServer thriftServer = new ThriftServer();
        thriftServer.setWeight(ThriftConstant.WEIGHT);
        thriftServer.setHostAndPort(HostAndPort.fromParts(serviceInfo.getIp(), serviceInfo.getPort()));
        return thriftServer;
    }
}
