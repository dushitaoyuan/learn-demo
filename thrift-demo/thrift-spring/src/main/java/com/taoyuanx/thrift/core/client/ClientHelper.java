package com.taoyuanx.thrift.core.client;

import com.google.common.collect.ImmutableList;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHelper {

    private static DriftNettyMethodInvokerFactory<?> methodInvokerFactory;

    private static Map<Class, Object> PROXY_MAP = new ConcurrentHashMap<>();

    public static ThriftClientConfig toThriftClientConfig(ThriftClient thriftClient, Class serviceInterfaceClass) {
        try {
            ThriftClientConfig thriftClientConfig = new ThriftClientConfig();
            thriftClientConfig.setRequestTimeout(thriftClient.requestTimeOut());
            thriftClientConfig.setServiceInterfaceName(serviceInterfaceClass.getName());
            thriftClientConfig.setServiceInterfaceClass(serviceInterfaceClass);
            thriftClientConfig.setServerList(thriftClient.serverList());
            return thriftClientConfig;
        } catch (Exception e) {
            throw new MyThriftExceptioin("配置异常", e);
        }
    }

    public static <T> T createProxy(Class<T> serviceInterface, List<HostAndPort> hostAndPortList, ThriftClientConfig thriftClientConfig) {
        if (!PROXY_MAP.containsKey(serviceInterface)) {
            List<HostAndPort> addresses = ImmutableList.copyOf(hostAndPortList);
            SimpleAddressSelectorConfig simpleAddressSelectorConfig = new SimpleAddressSelectorConfig();
            simpleAddressSelectorConfig.setAddressesList(addresses);
            ThriftCodecManager codecManager = new ThriftCodecManager();
            AddressSelector addressSelector = new SimpleAddressSelector(simpleAddressSelectorConfig);
            DriftNettyClientConfig config = toDriftNettyClientConfig(thriftClientConfig);
            methodInvokerFactory = DriftNettyMethodInvokerFactory
                    .createStaticDriftNettyMethodInvokerFactory(config);
            DriftClientFactory clientFactory = new DriftClientFactory(codecManager, methodInvokerFactory, addressSelector);
            DriftClient<T> driftClient = clientFactory.createDriftClient(serviceInterface);
            PROXY_MAP.put(serviceInterface, driftClient.get());
        }

        return (T) PROXY_MAP.get(serviceInterface);
    }

    public static DriftNettyClientConfig toDriftNettyClientConfig(ThriftClientConfig thriftClientConfig) {
        DriftNettyClientConfig driftNettyClientConfig = new DriftNettyClientConfig();
        return driftNettyClientConfig;
    }

    public static void close() {
        if (Objects.nonNull(methodInvokerFactory)) {
            methodInvokerFactory.close();
        }
    }

}
