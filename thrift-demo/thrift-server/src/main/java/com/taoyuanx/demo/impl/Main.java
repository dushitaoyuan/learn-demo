package com.taoyuanx.demo.impl;

import com.google.common.collect.ImmutableSet;
import com.taoyuanx.demo.api.DemoService;
import io.airlift.drift.codec.ThriftCodecManager;
import io.airlift.drift.server.DriftServer;
import io.airlift.drift.server.DriftService;
import io.airlift.drift.server.stats.NullMethodInvocationStatsFactory;
import io.airlift.drift.transport.netty.server.DriftNettyServerConfig;
import io.airlift.drift.transport.netty.server.DriftNettyServerTransportFactory;

/**
 * @author dushitaoyuan
 * @date 2021/4/1117:12
 */
public class Main {
    public static void main(String[] args) {
        DemoService service = new DemoServiceImpl();
        DriftService driftService = new DriftService(service);

        DriftNettyServerConfig serverConfig = new DriftNettyServerConfig();
        serverConfig.setPort(9090);
        serverConfig.setWorkerThreadCount(10);
        DriftServer driftServer = new DriftServer(
                new DriftNettyServerTransportFactory(serverConfig),
                new ThriftCodecManager(),
                new NullMethodInvocationStatsFactory(),
                ImmutableSet.of(driftService),
                ImmutableSet.of());

        driftServer.start();
    }
}
