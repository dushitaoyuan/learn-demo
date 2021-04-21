package com.taoyuanx.thrift.core.client;

import com.google.common.net.HostAndPort;
import com.taoyuanx.thrift.core.exception.MyThriftExceptioin;
import io.airlift.drift.client.DriftClient;
import io.airlift.drift.client.DriftClientFactory;
import io.airlift.drift.client.address.AddressSelector;
import io.airlift.drift.client.address.SimpleAddressSelector;
import io.airlift.drift.client.address.SimpleAddressSelectorConfig;
import io.airlift.drift.codec.ThriftCodecManager;
import io.airlift.drift.transport.netty.client.DriftNettyClientConfig;
import io.airlift.drift.transport.netty.client.DriftNettyMethodInvokerFactory;
import io.airlift.units.Duration;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ClientProxyFactory {


    private static Map<Class, Object> PROXY_MAP = new ConcurrentHashMap<>();

    private static List<DriftNettyMethodInvokerFactory> METHOD_INVOKER_FACTORY_LIST = Collections.synchronizedList(new ArrayList<>());
    private ThriftCodecManager codecManager = new ThriftCodecManager();

    public ClientProxyFactory() {
    }


    public <T> T getServiceProxy(ThriftClientConfig thriftClientConfig) {
        Class serviceInterfaceClass = thriftClientConfig.getServiceInterfaceClass();
        if (!PROXY_MAP.containsKey(serviceInterfaceClass)) {
            List<HostAndPort> hostAndPortList = Arrays.stream(thriftClientConfig.getServerList())
                    .map(HostAndPort::fromString).collect(Collectors.toList());
            SimpleAddressSelectorConfig simpleAddressSelectorConfig = new SimpleAddressSelectorConfig();
            simpleAddressSelectorConfig.setAddressesList(hostAndPortList);
            AddressSelector addressSelector = new SimpleAddressSelector(simpleAddressSelectorConfig);
            DriftNettyMethodInvokerFactory<?> methodInvokerFactory = DriftNettyMethodInvokerFactory
                    .createStaticDriftNettyMethodInvokerFactory(toDriftNettyClientConfig(thriftClientConfig));
            METHOD_INVOKER_FACTORY_LIST.add(methodInvokerFactory);
            DriftClient<T> driftClient = new DriftClientFactory(codecManager, methodInvokerFactory, addressSelector)
                    .createDriftClient(serviceInterfaceClass);
            PROXY_MAP.put(serviceInterfaceClass, driftClient.get());
        }
        return (T) PROXY_MAP.get(serviceInterfaceClass);
    }

    public ThriftClientConfig buildThriftClientConfig(ThriftClient thriftClient, Class serviceInterfaceClass) {
        try {
            ThriftClientConfig clientConfig = new ThriftClientConfig();
            clientConfig.setRequestTimeout(thriftClient.requestTimeOut());
            clientConfig.setServiceInterfaceName(serviceInterfaceClass.getName());
            clientConfig.setServiceInterfaceClass(serviceInterfaceClass);
            clientConfig.setServerList(thriftClient.serverList());
            return clientConfig;
        } catch (Exception e) {
            throw new MyThriftExceptioin("配置异常", e);
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
        if (Objects.nonNull(METHOD_INVOKER_FACTORY_LIST)) {
            METHOD_INVOKER_FACTORY_LIST.stream().forEach(DriftNettyMethodInvokerFactory::close);
        }
    }

}
