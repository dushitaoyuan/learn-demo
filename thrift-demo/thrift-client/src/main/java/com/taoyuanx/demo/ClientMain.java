package com.taoyuanx.demo;

import com.google.common.collect.ImmutableList;
import com.google.common.net.HostAndPort;
import com.taoyuanx.demo.api.DemoService;
import com.taoyuanx.demo.api.dto.HelloRequest;
import io.airlift.drift.client.DriftClient;
import io.airlift.drift.client.DriftClientFactory;
import io.airlift.drift.client.address.AddressSelector;
import io.airlift.drift.client.address.SimpleAddressSelector;
import io.airlift.drift.client.address.SimpleAddressSelectorConfig;
import io.airlift.drift.codec.ThriftCodecManager;
import io.airlift.drift.transport.netty.client.DriftNettyClientConfig;
import io.airlift.drift.transport.netty.client.DriftNettyMethodInvokerFactory;

import java.util.List;

/**
 * @author dushitaoyuan
 * @date 2021/4/1118:13
 */
public class ClientMain {
    public static void main(String[] args) {
        List<HostAndPort> addresses = ImmutableList.of(HostAndPort.fromParts("localhost", 9090),HostAndPort.fromParts("localhost", 9092));


        SimpleAddressSelectorConfig simpleAddressSelectorConfig = new SimpleAddressSelectorConfig();
        simpleAddressSelectorConfig.setAddressesList(addresses);
        ThriftCodecManager codecManager = new ThriftCodecManager();
        AddressSelector addressSelector = new SimpleAddressSelector(simpleAddressSelectorConfig);
        DriftNettyClientConfig config = new DriftNettyClientConfig();

// methodInvokerFactory must be closed
        DriftNettyMethodInvokerFactory<?> methodInvokerFactory = DriftNettyMethodInvokerFactory
                .createStaticDriftNettyMethodInvokerFactory(config);

// client factory
        DriftClientFactory clientFactory = new DriftClientFactory(codecManager, methodInvokerFactory, addressSelector);
        DriftClient<DemoService> driftClient = clientFactory.createDriftClient(DemoService.class);
        DemoService demoService = driftClient.get();
        System.out.println(demoService.hello("111"));
        HelloRequest helloRequest = new HelloRequest();
        helloRequest.setName("dushitaoyuan");
        helloRequest.setValue("dushitaoyuan");
        System.out.println(demoService.hello2(helloRequest).getMsg());
        methodInvokerFactory.close();
    }
}
